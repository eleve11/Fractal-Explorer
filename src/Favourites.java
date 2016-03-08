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
    private List<Complex> favList;

    //it's a singleton because there should only be one instance of it in this app
    private static final Favourites instance = new Favourites();

    //private constructor because is a singleton
    private Favourites(){
        file = new File("favourites");
        favComboBox = new JComboBox<String>();
        favList = new ArrayList<Complex>();

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

    //guarantees that the file exists by creating it
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
        favList.clear();
        if(br.ready()){
            while((line=br.readLine()) != null){
                if(line.split(":").length>2)
                    throw new IllegalArgumentException("The favourites is formatted incorrectly");
                double real = Double.parseDouble(line.split(":")[0]);
                double img = Double.parseDouble(line.split(":")[1]);
                favComboBox.addItem(new Complex(real,img).toString());
                favList.add(new Complex(real,img));
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
        favList.add(c);

        //add to file
        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
        output.write(c.getX()+":"+c.getY());
        output.newLine();
        output.flush();
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

        //what happens to the combo box when it is removed the item that shows
        String toRemove = favComboBox.getItemAt(favComboBox.getSelectedIndex());
        if(toRemove!=null && toRemove.equals(c.toString()))
            favComboBox.setSelectedIndex(-1);

        //remove from List
        favComboBox.removeItem(c.toString());
        favList.remove(c);


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
        bw.flush();
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
    public List<Complex> getFavList() {
        return favList;
    }

    /*
     * return the gui component object that represents the favourites
     */
    public JComboBox<String> getFavourites(){
        return favComboBox;
    }
}