package com.noldos;

import java.nio.file.Path;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {        
        if(args.length == 0){
            System.err.println("Usage: java -jar app.jar <path-to-json>");
            System.exit(1);
        }
        try{
            Path path = Path.of(args[0]);
            ContractLoader contractloader = new ContractLoader();
            ReportPrinter printer = new ReportPrinter();
            String formatprinted = printer.format(contractloader.load(path), List.of());
            System.out.println(formatprinted);
            System.exit(0);
        }
        catch(ContractLoadException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
