import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MusicSorter {

    public static void main(String[] args) throws IOException {
        //File sourceFolder = new File("C:\\Users\\igor_makarychev\\Downloads\\");
        //File destinationFolder = new File("E:\\");
        //File destinationNewFolder = new File(destinationFolder+"!New\\");

        File sourceFolder = new File("/Users/igormakarychev/Downloads/toFlash/");
        File destinationFolder = new File("Volume/No name/");
        File destinationNewFolder = new File(destinationFolder+"/!New/");

        // Rename existing files
        //processFiles(sourceFolder,destinationFolder);

        // Sort existing files from "!New" folder to appropriate named (A to Z) folders
        processFiles(destinationNewFolder,destinationFolder);

        // Move new files to "!New" folder
        processFiles(sourceFolder,destinationNewFolder);
    }

    public static void processFiles(final File sourceFolder, final File destinationFolder) throws IOException {
        if (sourceFolder.list()!=null) {
            for (final File fileEntry : sourceFolder.listFiles()) {
                if (fileEntry.isFile() & getFileExtension(fileEntry).equals(".mp3")) {
                    String newName = translitRusToEng(getFileName(fileEntry));
                    String extension = getFileExtension(fileEntry);
                    File newFile;

                    if (destinationFolder.getName().equals("!New")) {
                        newFile = new File(destinationFolder + "/" + newName + extension);
                    }
                    else {
                        File folderForMoving = findProperFolderByFirstChar(new File(newName), destinationFolder);
                        newFile = new File(folderForMoving + "/" + newName.toUpperCase() + extension);
                    }

                    System.out.println("File: " + fileEntry.getName() + " - moved to folder: " + newFile.getPath());
                    moveFileToFolder(fileEntry, newFile);
                }
            }
        }
    }

    public static File findProperFolderByFirstChar (File fileToMove, final File newFolder) {

        File baseFolder = new File (newFolder.getPath());
        File returnFolder = null;

        if (Character.isDigit(fileToMove.getName().substring(0,1).charAt(0))) {
            returnFolder = new File(baseFolder+"/0-9");
        }
        else {
            returnFolder = new File(baseFolder + "/" + fileToMove.getName().substring(0, 1).toUpperCase());
        }

        return returnFolder;
    }

    //This method will simply move specified file to another location
    public static void moveFileToFolder(final File oldFile, final File newFile) throws IOException {
        createFolder(new File(newFile.getParent()));
        Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    //Change cyrillic symbols to latin
    public static String translitRusToEng(String text) {
        //char[] text_lowerCase = text.toLowerCase().toCharArray();
        String[] text_lowerCase = text.toLowerCase().split("");
        StringBuilder builder = new StringBuilder();

        //char[] abcCyr = {'а', 'б', 'в', 'г', 'д', 'е',  'ё', 'ж', 'з', 'и','й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ь', 'ы', 'э', 'ю', 'я', 'і', 'ї', 'ґ'};
        String[] abcCyr = {"а", "б", "в", "г", "д", "е",  "ё", "ж", "з", "и","й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ь", "ы", "э", "ю", "я", "і", "ї", "ґ"};
        String[] abcLat = {"a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch","sh", "sch", "'","'", "y", "e", "ju", "ja", "i", "i", "g"};

        for (int i = 0; i < text_lowerCase.length; i++) {

            String symbol_to_append = null;

            for (int x = 0; x < abcCyr.length; x++) {
                if (text_lowerCase[i].equals(abcCyr[x])) {
                    symbol_to_append = abcLat[x];
                    x = abcCyr.length;
                } else {
                    if (text_lowerCase[i].equals("_")) {
                        symbol_to_append = " ";
                        x = abcCyr.length;
                    }
                }
            }

            builder.append(symbol_to_append == null ? text_lowerCase[i] : symbol_to_append);
        }

        return builder.toString().toUpperCase();
    }

    public static String getFileName(final File file) {
        String fileName = "";
        int i = file.getName().lastIndexOf(".");
        if (i > 0) {
            fileName = file.getName().substring(0,i);
        }
        return fileName.toLowerCase();
    }

    public static String getFileExtension(final File file) {
        String extension = "";
        int i = file.getName().lastIndexOf(".");
        if (i > 0) {
            extension = file.getName().substring(i);
        }
        return extension.toLowerCase();
    }

    public static void createFolder(final File folder){
        if (!folder.exists()) {
            System.out.println("Creating directory: " + folder);
            folder.mkdir();
        }
    }

}