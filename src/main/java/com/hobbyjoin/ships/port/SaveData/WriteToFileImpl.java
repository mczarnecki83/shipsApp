package com.hobbyjoin.ships.port.SaveData;

import com.hobbyjoin.ships.model.DataOut;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriteToFileImpl implements DataOut {
    @Override
    public void saveData(String data,String target) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(target);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        printWriter.println(data);
        printWriter.close();
    }
}
