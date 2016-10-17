package net.anei.cadpage.parsers.FL;



import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class FLPensacolaParser extends FieldProgramParser {
	
  
  public FLPensacolaParser() {
    super("PENSACOLA", "FL",
        "Call_Number:ID! Units:UNIT! Complaint:CALL! Location:PLACE! Address:ADDR! Xst_1:X! Xst_2:SKIP! City:CITY! State:ST! CAddress1:SKIP! CAddress2:SKIP! Loc_Display:SKIP! Time_Dispatched:DATETIME! Narrative:INFO! This_Unit:SKIP!");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("_", " "); 
    if (!parseFields(body.split("\\|"), 14, data)) return false;  
    if (data.strState.equals("FL")) data.strState = ""; 
    return true;
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(",")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern PLACE_X_PTN = Pattern.compile("\\bX2\\[([^\\[\\]]+)\\]$");
  private static final Pattern PHONE_PTN = Pattern.compile("\\b(?:\\d{3}-?)?\\d{3}-?\\d{4}$");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_X_PTN.matcher(field);
      if (match.find()) {
        data.strCross = match.group(1).trim();
        field = field.substring(0,match.start()).trim();
      }
      match = PHONE_PTN.matcher(field);
      if (match.find()) {
        data.strPhone = match.group().trim();
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE PHONE X";
    }
  }


  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    return super.getField(name);
  }
}