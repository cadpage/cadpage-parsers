package net.anei.cadpage.parsers.OK;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKTulsaBParser extends FieldProgramParser {
  
  public OKTulsaBParser() {
    super("TULSA", "OK",
          "MARK DATETIME MARK! CID:ID! CALL:CALL! NAME/LOCATION:PLACE! ADDRESS:ADDR! CITY:CITY! INFO/NOTES:INFO! INFO+ CALL_BACK_#:PHONE INFO+? MARK");
  }
  
  @Override
  public String getFilter() {
    return "fax@connectionsok.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Your Messages")) return false;
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField("===+\\d*===+", true);
    if (name.equals("DATETIME")) return new  MyDateTimeField();
    return super.getField(name);
  }
  
  private static DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE dd-MMM-yy hh:mmaa");
  private class MyDateTimeField extends DateTimeField {
    MyDateTimeField() {
      super(DATE_TIME_FMT, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field+'m', data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    
    // Google apparently has a much more restrictive definition of Turley
    // than dispatch does.  Things seem to map better if we refer to everything
    // in TULSA"
    if (city.equals("TURLEY")) city = "TULSA";
    return city;
  }
}
