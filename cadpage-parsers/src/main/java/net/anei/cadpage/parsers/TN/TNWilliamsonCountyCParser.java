package net.anei.cadpage.parsers.TN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNWilliamsonCountyCParser extends FieldProgramParser {
  
  public TNWilliamsonCountyCParser() {
    super(TNWilliamsonCountyParser.CITY_LIST, "WILLIAMSON COUNTY", "TN", 
          "( PN:CALL! ADD:ADDR! ( END | ( XST:X! | CITY:CITY! LAT:GPS1/d! LON:GPS2/d! APT:APT! X_ST:X! LOCATION:PLACE! TIME:DATETIME! CN:ID! ) UNITS:UNIT! END ) " +
          "| CALL! ADD:ADDR/S! X:X? LAT:GPS1/d! LON:GPS2/d! TAC:CH? #:ID! UNITS:UNIT! LOC:PLACE! END )");
  }
  
  @Override
  public String getFilter() {
    return "911-Center@williamson-tn.org,911-center@brentwoodtn.gov,911-Center@williamsoncounty-tn.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=[ap]m)(?=CN:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n_____");
    if (pt >= 0) body = body.substring(0, pt).trim();
    body = MISSING_BLANK_PTN.matcher(body).replaceFirst(" ");
    body = body.replace("ADD:",  " ADD:").replace(" #", " #:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("(.*?) ([,\\d]+)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (isLastField()) {
        Matcher match = ADDR_UNIT_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
          data.strUnit =  match.group(2);
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT?";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d [ap]m)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
}
