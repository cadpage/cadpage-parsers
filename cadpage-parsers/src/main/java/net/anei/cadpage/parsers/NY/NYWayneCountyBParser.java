package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYWayneCountyBParser extends FieldProgramParser {

  public NYWayneCountyBParser() {
    super(CITY_CODES, "WAYNE COUNTY", "NY",
          "( DISPATCH_REPORT! CFS_Number:ID! SKIP! CODECALL! LOCATION:ADDRCITY! Apt:APT! Bldg:BLDG! RECEIVED:DATETIME! Cross_Streets:X? COMPLAINANT:NAME! CALLBACK_NO:PHONE! BOX:BOX! INFO/N+"+
          "| RESPONDING_UNIT_TIMES_REPORT/R! SKIP! CFS_Number:ID! SKIP+ Incident_Address:ADDRCITY! Apt:APT! Bldg:BLDG! City:CITY! Inc_Code:CODE! Inc_Desc:CALL! In_Prog:SKIP! Descriptive:PLACE! Caller_Name:NAME! Caller_Phone:PHONE! Caller_address:SKIP! Caller_Residence_Phone:SKIP! Received:DATETIME! INFO/N+ Primary_Unit:UNIT! INFO/N+ "+
          "| FROM_ACTIVE911/R! SRC:SRC! PRI:PRI! ID:ID! PLACE:PLACE! INFO:INFO/N+ PH:PHONE? INFO/N+ ( DATE:DATE! INFO/N+ TIME:TIME! | TIME:TIME! INFO/N+ DATE:DATE! ) INFO/N+ CODE:CODE! INFO/N+ CALL:CALL! DST:ST! APT:APT! CITY:CITY! MAP:MAP! ADDR:ADDR! INFO/N+ UNIT:UNIT! )");
  }

  @Override
  public String getFilter() {
    return "ripandrun@co.wayne.ny.us,noreply@alert1-or.active911.com,noreply@alert1-tx.active911.com,noreply@alert1-tx.yourfirstdue.com,noreply@alert1-or.yourfirstdue.com";
  }

  public boolean parseMsg(String subject, String body, Data data) {
    //Anything without DISPATCH REPORT for subject is a RUN REPORT
//    if (!subject.equals("DISPATCH REPORT")) data.msgType = MsgInfo.MsgType.RUN_REPORT;
    return parseFields(body.split("\n\\s*"), data);
  }

  @Override
  public String getProgram() {
    String prog = super.getProgram();
    //move INFO to end of program string so shorter fields can be read above it
    if (prog.endsWith("INFO")) return prog;
    return prog.replace(" INFO ", " ") + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DISPATCH_REPORT")) return new SkipField("DISPATCH REPORT", true);
    if (name.equals("CODECALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("APT")) return new AptField("(?:= )?(.*)");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("RESPONDING_UNIT_TIMES_REPORT")) return new SkipField("RESPONDING UNIT TIMES REPORT", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("FROM_ACTIVE911")) return new SkipField("From Active911", true);
    return super.getField(name);
  }

  //remove brackets from city code
  private static Pattern CODE_CALL = Pattern.compile("(.+?)\\s+(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = CODE_CALL.matcher(field);
      if (mat.matches()) {
        data.strCode = mat.group(1);
        data.strCall = mat.group(2);
      } else data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  //convert ADDR( [CITY])? to ADDR(, CITY)?
  private static Pattern ADDR_CITY = Pattern.compile("(.+?)\\s*\\[(.*)\\]");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = ADDR_CITY.matcher(field);
      if (mat.matches()) super.parse(mat.group(1)+", "+mat.group(2), data);
      else super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      //parse city code
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
      //if no city parsed, field must either be a plain city or new city code
      if (data.strCity.length() == 0) data.strCity = field;
    }
  }

  //just replace * with /
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace(" * ", " / "), data);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = PK_PTN.matcher(sAddress).replaceAll("PARK");
    return super.adjustMapAddress(sAddress);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "BUTLER",    "BUTLER",
      "CLYDE_V",   "CLYDE VILLAGE",
      "FARMINGTO", "FARMINGTON",
      "FRMINGTON", "FARMINGTON",
      "GALEN",     "GALEN",
      "LYONS_V",   "LYONS VILLAGE",
      "MACEDON",   "MACEDON",
      "MACEDON_V", "MACEDON VILLAGE",
      "MARION",    "MARION",
      "NEWARK_V",  "NEWARK", //VILLAGE unnecessary because no town named Newark to distinguish from
      "ONTARIO",   "ONTARIO",
      "PALMYRA",   "PALMYRA",
      "PALMYRA_V", "PALMYRA VILLAGE",
      "RED_CREEK", "RED CREEK",
      "ROSE",      "ROSE",
      "SAVANNAH",  "SAVANNAH",
      "SODUS",     "SODUS",
      "SODUS_PT",  "SODUS POINT",
      "SODUS_V",   "SODUS VILLAGE",
      "WALWORTH",  "WALWORTH",
      "WATERLOO",  "WATERLOO",
      "WAYNE COUNTY", "WAYNE COUNTY",
      "WILLIAMSO", "WILLIAMSON",
      "WOLCOTT",   "WOLCOTT",
      "WOLCOTT_V", "WOLCOTT",
  });

}
