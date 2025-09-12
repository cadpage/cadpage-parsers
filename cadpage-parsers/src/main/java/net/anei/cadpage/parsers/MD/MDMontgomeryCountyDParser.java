package net.anei.cadpage.parsers.MD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;

public class MDMontgomeryCountyDParser extends FieldProgramParser {
  
  public MDMontgomeryCountyDParser() {
    super("MONTGOMERY COUNTY", "MD", 
          "CALL:CALL! ADDR:ADDRCITYST! CITY:SKIP! UNITS:UNIT! DATE:DATETIME! BOX:BOX! INFO:INFO! END");
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }

}
