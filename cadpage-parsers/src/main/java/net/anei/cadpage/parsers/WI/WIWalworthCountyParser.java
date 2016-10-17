package net.anei.cadpage.parsers.WI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIWalworthCountyParser extends FieldProgramParser {
  
  public WIWalworthCountyParser() {
    super("WALWORTH COUNTY", "WI", 
          "CFS:CALL! Location:ADDR! Call#:ID! Units_Dispatched:UNIT! Stations_Dispatched:SRC! Report_Date/Time:DATETIME!");
  }
  
  @Override
  public String getFilter() {
    return "lgpdrms@genevaonline.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Phoenix Notification")) return false;
    body = body.replace('\t', ' ');
    return parseFields(body.split("\n"), data);
  }
  
  private static DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (apt.equals("BLDG")) apt = "";
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = field.replace(" ", "");
    }
  }
}
