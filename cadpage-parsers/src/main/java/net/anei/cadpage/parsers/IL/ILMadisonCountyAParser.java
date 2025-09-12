package net.anei.cadpage.parsers.IL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ILMadisonCountyAParser extends DispatchH05Parser {
  
  public ILMadisonCountyAParser() {
    super("MADISON COUNTY", "IL", 
          "EMPTY ( Mitchel_Fire_R&R_from_New_World_Systems%EMPTY Fire_Call_Type:CALL! Call_Address:ADDRCITY/S6 Common_Name:PLACE! Cross_Streets:X! " + 
                      "Nature_of_Call:INFO! Narrative:EMPTY! INFO_BLK+ Call_Date/Time:DATETIME! Caller_Name:NAME! Caller_Phone_#:PHONE! Status_Times:EMPTY! TIMES+ " + 
                "| CALL_RECEIVED_AT EMPTY CALL EMPTY " + 
                    "( SELECT/1 ( ADDRCITY/S6Z EMPTY/Z SKIP EMPTY/Z ID/Y EMPTY X EMPTY INFO EMPTY EMPTY! EMPTY? INFO_BLK+? TIMES+ " + 
                               "| PLACE EMPTY ADDRCITY/S6 EMPTY X EMPTY INFO EMPTY ID/Y! EMPTY TIMES+ " +
                               ") " +
                    "| SELECT/2 ADDRCITY/S6 EMPTY PLACE EMPTY X EMPTY INFO/N EMPTY INFO_BLK/Z+? DATETIME EMPTY NAME EMPTY PHONE EMPTY TIMES! TIMES+ https:GPS " +
                    "| FAIL " +
                    ") " +
                ") EMPTY+? END");
    setPreserveWhitespace(true);
  }
  
  @Override
  public String getFilter() {
    return "ripnrun@madisoncountyil.gov";
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d");

  @Override
  public boolean parseFields(String[] flds, Data data) {
    boolean setSelect =  false;
    int ndx;
    for (ndx = 0; ndx<flds.length; ndx++) {
      String fld = flds[ndx].trim();
      flds[ndx] = fld;
      if (!setSelect) {
        if (fld.startsWith("[") && fld.endsWith("]")) {
          setSelect = true;
          setSelectValue("1");
        } else if (DATE_TIME_PTN.matcher(fld).matches()) {
          setSelect = true;
          setSelectValue("2");
        }
      }
      if (fld.equals("Disclaimer")) break;
    }
    
    if (ndx < flds.length) {
      String[] flds2 = new String[ndx];
      System.arraycopy(flds, 0, flds2, 0, ndx);
      flds = flds2;
    }
    
    return super.parseFields(flds, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_RECEIVED_AT")) return new SkipField("(?i)Call Rece?ie?ved at:?", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField(".*query=(.*)", true);
    return super.getField(name);
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    return apt.equals("HWY") ||
           apt.equals("HOUSE") ||
           apt.startsWith("&") ||
           apt.startsWith("/");
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
}
