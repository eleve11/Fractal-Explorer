import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent the Favourite list using a JComboBox
 * is a singleton class that manages the favourite juliaSets
 * by reading and writing on a file
 */
public class Favourites
{
    private BufferedReader br;
    private File file;
    private JComboBox<String> favComboBox;

    //it's a singleton
    public static final Favourites instance = new Favourites();

    public Favourites(){
        file = new File("favourites");
        favComboBox = new JComboBox<String>();

        try {
            ensureFileExistance(file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Reader: File cannot be found or created");
        }

        try {
            updateList();
        } catch (IOException e) {
            System.err.println(e.getMessage()+"\nCannot update list");
        }
    }

    private void ensureFileExistance(File file) throws IOException {
        if(!file.exists())
            file.createNewFile();
    }

    /*
     * update the list reading from the file
     */
    private void updateList() throws IOException, IllegalArgumentException{
        br = new BufferedReader(new FileReader(file));
        String line;
        favComboBox.removeAllItems();
        if(br.ready()){
            while((line=br.readLine()) != null){
                if(line.split(":").length>2)
                    throw new IllegalArgumentException("The favourites is formatted incorrectly");
                double real = Double.parseDouble(line.split(":")[0]);
                double img = Double.parseDouble(line.split(":")[1]);
                favComboBox.addItem(new Complex(real,img).toString());
            }
        }
        br.close();
    }

    /*
     * add a complex number to the favourite file
     * and update the list
     */
    public boolean add(Complex c) throws IOException
    {
        //don't allow adding duplicates
        if(this.contains(c))
            return false;

        //add to List
        favComboBox.addItem(c.toString());

        //add to file
        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
        output.write(c.getX()+":"+c.getY());
        output.newLine();
        output.close(); //close stream
        return true;
    }

    /*
     * remove Complex number from list
     * it copies all the non matching lines to a temp file then
     * replaces the main file with the temp
     */
    public void remove(Complex c) throws IOException, IllegalArgumentException
    {
        if(!this.contains(c))
            throw new IllegalArgumentException("Complex number not found");
        //remove from List
        favComboBox.removeItem(c.toString());

        //prepare IO
        File temp = new File("temp");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        br = new BufferedReader(new FileReader(file));


        /*
         * loop through the file and rewrite only the line that
         * are not equal to the one we want to delete
         */
        String delete = c.getX()+":"+c.getY();
        String line;
        while((line=br.readLine())!=null){
            if(!line.trim().equals(delete)) {
                bw.write(line + System.getProperty("line.separator"));
            }
        }
        //close streams
        bw.close();
        br.close();

        //replace the contents of file with the ones of temp
        temp.renameTo(file);
    }


    /*
     * return true if the complex number is in the Favourites List
     */
    public boolean contains(Complex c){
        for(int i=0;i<favComboBox.getItemCount();i++){
            if(favComboBox.getItemAt(i).equals(c.toString()))
                return true;
        }
        return false;
    }

    /*
     * return the singleton instance of Favourites
     */
    public static Favourites getInstance() {
        return instance;
    }

    /*
     * return the list of complex number that are favourites
     */
    public JComboBox<String> getFavourites(){
        return favComboBox;
    }
}