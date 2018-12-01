package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PALehighCountyDParser extends DispatchH05Parser {
  
  public PALehighCountyDParser() {
    super("LEHIGH COUNTY", "PA", 
          "( MARKER! ( CALL_DATE/TIME:DATETIME LOC:ADDRCITY! XSTS:X! LAT/LON:GPS! BOX:BOX! TYPE:SKIP! DESCRIPTION:CALL! NATURE_OF_CALL:CALL1! " + 
                            "( NARR:EMPTY! INFO_BLK+ UNITS:UNIT! UNIT1<+ " + 
                            "| UNITS:EMPTY! UNIT1<+ NARRATIVE:EMPTY! INFO_BLK+ " + 
                            ") " + 
                     "| LOC:ADDRCITY! TYPE:SKIP! DESCRIPTION:CALL! NATURE_OF_CALL:CALL1! CALLER_NAME:NAME! CALLER_PHONE:PHONE! NARR:EMPTY! INFO_BLK+ UNITS:UNIT! UNIT1<+ " +
                     ") INCIDENT_#:ID? TIMES:EMPTY! TIMES+ " +
           "| CALL CALL/SDS CALL/SDS ADDRCITY! X_STS:X! GPS! INFO_BLK+? INC#:ID! TIMES+ " +
           ")",
          "tr");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lehighcounty.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern TRAIL_PIPE_PTN = Pattern.compile("(.*?)[ |]+");
  
  @Override
  protected boolean parseFields(String[] fields, Data data) {
    for (int ndx = 0; ndx < fields.length; ndx++) {
      String fld = fields[ndx];
      Matcher match = TRAIL_PIPE_PTN.matcher(fld);
      if (match.matches()) fields[ndx] = match.group(1);
    }
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARKER")) return new SkipField("LEHIGH COUNTY COMMUNICATIONS / 9-1-1 RIP AND RUN NOTIFICATION", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("UNIT1")) return new MyUnit1Field();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('|',  ',').replace('/', ',');
      super.parse(field, data);
    }
  }
  
  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" ", "");
      super.parse(field, data);
    }
  }
  
  private class MyUnit1Field extends UnitField {
    
    int colNdx = 0;
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<|tr|>")) {
        colNdx = 0;
        return;
      }
      
      if (++colNdx == 2) {
        data.strUnit = append(data.strUnit, ",", field);
      }
    }
  }

}
