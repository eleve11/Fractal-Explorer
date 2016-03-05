import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represent the Favourite list
 * is a singleton class that manages the favourite juliaSets
 * by reading and writing on a file
 */
public class Favourites
{
    private List<Complex> fav;
    private BufferedReader bf;
    private BufferedWriter output;
    private File file;
    //it's a singleton
    public static final Favourites instance = new Favourites();

    public Favourites(){
        fav = new ArrayList<Complex>();
        file = new File("favourites");

        try {
            bf = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.err.println("File cannot be found or created");
        }

        try {
            output = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            System.err.println("File cannot be found or created");
        }

        try {
            updateList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * update the list reading from the file
     */
    private void updateList() throws IOException{
        String line;
        fav.clear();
        if(bf.ready()){
            while((line=bf.readLine()) != null){
                double real = Double.parseDouble(line.split(":")[0]);
                double img = Double.parseDouble(line.split(":")[1]);
                fav.add(new Complex(real,img));
            }
        }
    }

    /*
     * add a complex number to the favourite file
     * and update the list
     */
    public void add(Complex c) throws IOException
    {
        fav.add(c);
        output.write(c.getX()+":"+c.getY());
        output.newLine();
    }

    /*
     * remove Complex number from list
     */
    public void remove(Complex c) throws IOException
    {
        //remove from List
        if(fav.contains(c))
            fav.remove(c);

        /*
         * loop through the file and rewrite only the line that
         * are not equal to the one we want to delete
         */
        String delete = c.getX()+":"+c.getY();
        String line;
        while((line=bf.readLine())!=null){
            if(!line.trim().equals(delete))
                output.write(line + System.getProperty("line.separator"));
        }

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
    public List<Complex> getFavourites(){
        return fav;
    }

    /*
     * close the RW streams
     */
    public void close(){
        try {
            output.close();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FavTest{
    Favourites fav = Favourites.getInstance();
    Random random = new Random();
    public void testAdd(){

    }
}
