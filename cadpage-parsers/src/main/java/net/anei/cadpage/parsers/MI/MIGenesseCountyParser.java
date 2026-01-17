package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIGenesseCountyParser extends FieldProgramParser {

  public MIGenesseCountyParser() {
    super(CITY_CODES,"GENESSE COUNTY","MI",
          "DATE_TIME:SKIP! EVENT_NUM:ID! EID:ID/L! CALLTAKER_ID:SKIP! CALL_SOURCE:SKIP CALLER_NAME:NAME CALLER_NUMBER:PHONE CALLER_ADDRESS:SKIP " +
                "AGENCY_ID:SRC! PRIORITY:PRI! TYPE:CODE! SUBTYPE:CODE/L! DESCRIPTION:CALL! SUB_DESCRIPTION:CALL/L PRIMARY_EMPLOYEE_ID:SKIP " +
                "REVISION_NUMBER:SKIP DISPATCHED_TIME:DATETIME! VERIFIED_ADDRESS:SKIP! MUN:CITY LOCATION:ADDR! COMMENTS:INFO/N! " +
                "ESZ:MAP! X_COORD:SKIP! Y_COORD:SKIP! LATITUDE:GPS1! LONGITUDE:GPS2! jurisdiction:SKIP ori:SKIP MUTUAL_AID_NOTES:INFO/N! ",
                FLDPROG_DOUBLE_UNDERSCORE | FLDPROG_XML);
  }

  @Override
  public String getFilter() {
    return "iamresponding@geneseecounty911.org":
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD EVENT")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }


  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("default")) return;
      super.parse(field, data);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String extra = "";
      int pt = field.indexOf("::");
      if (pt >= 0) {
        extra = '(' + field.substring(pt+2).trim() + ')';
        field = field.substring(0,pt).trim();
      }
      pt = field.indexOf(":");
      if (pt >= 0) {
        data.strPlace = stripFieldStart(field.substring(pt+1).trim(), "@");
        field = field.substring(0,pt).trim();
      }
      field = append(field, " ", extra);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d?)-(\\d\\d?)T(\\d\\d:\\d\\d:\\d\\d)-\\d\\d:\\d\\d");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NONE")) return;
      for (String line : field.split("\n")) {
        line = line.trim();
        if (line.startsWith("WPH")) continue;
        super.parse(line, data);
      }
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BR", "BURTON CITY",
      "DA", "DAVISON CITY",
      "DT", "DAVISON TWP",
      "FO", "FOREST TWP",
      "GA", "GAINES TWP",
      "MM", "MT MORRIS TWP",
      "OT", "OTISVILLE",
      "OTG","",   // ????
      "RT", "RICHFIELD TWP",
      "TT", "THETFORD TWP"
  });
}
