package net.anei.cadpage.parsers.ZCAON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * 
 */
public class ZCAONMississaugaParser extends FieldProgramParser {
  
  private static final Pattern EXTRA_SPACES = Pattern.compile(" *\n *");
  private static final Pattern MISSING_BREAK = Pattern.compile("(?<!\n)(?=Tac:|X1:|X2:|Map:|Inc:)");
  private static final Pattern TAC_MISS_BREAK = Pattern.compile("(\nTac:\\S*?) +");
  
  private String selectType; 
  
  public ZCAONMississaugaParser() {
    super(CITY_CODES, "PEEL REGIONAL MUNICIPALITY", "ON",
           "CALL? ( SELECT/REG ADDR/y! Map:MAP! Tac:CH! UNIT! X1:X? X2:X? Inc:ID! | SELECT/LOC ADDR/y! X1:X? X2:X? Map:MAP? Tac:CH! CALL Inc:ID | PLACE ADDR/y DATETIME! )");
  }
  
  @Override
  public String getFilter() {
    return "pager@rsar.ca";
  }
  
  @Override
  public String getLocName() {
    return "Mississauga, ON";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    selectType = (body.indexOf('\n') > 0 ? "REG" : "COMP");
    body = EXTRA_SPACES.matcher(body).replaceAll("\n");
    body = MISSING_BREAK.matcher(body).replaceAll("\n");
    body = TAC_MISS_BREAK.matcher(body).replaceFirst("$1\n");
    body = body.replace("\nInc#", "\nInc:#");
    return parseFields(body.split("\n"), 4, data);
  }

  // There are some special constructs in the call description that
  // change the way everything is parsed
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (selectType.equals("COMP")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.contains(" TO REPLACE UNIT ")) {
        selectType = "REP";
      } else if (field.equals("*LOCATION CHANGE*")) {
        selectType = "LOC";
        field = "LOC CHANGE";
      } else {
        selectType = "REG";
      }
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  @Override
  public String getSelectValue() {
    if (selectType.equals("COMP")) return "REG";
    return selectType;
  }
  
  private static final Pattern DASH_CITY_PTN = Pattern.compile("\\b(\\w+)-([A-Z]{1,2})\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Compressed form is a special case
      // Anything else should be passed to the superclass parse method
      if (!selectType.equals("COMP")) {
        super.parse(field, data);
        return;
      }
      String city = null;
      StringBuffer sb = new StringBuffer();
      Matcher match = DASH_CITY_PTN.matcher(field);
      while (match.find()) {
        if (match.start() > 0) {
          city = match.group(2);
          match.appendReplacement(sb, "$1");
        }
      }
      match.appendTail(sb);
      field = sb.toString();
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, field, data);
      if (city != null) data.strCity = convertCodes(city, CITY_CODES);
    }
  }
  
  private static final Pattern TRIM_LEAD_X = Pattern.compile("^(?:0|[NSEW]{1,2} +OF) +");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TRIM_LEAD_X.matcher(field);
      if (match.find()) field = field.substring(match.end()).trim();
      if (field.endsWith("&")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(\\d+)(?: +(\\d\\d:\\d\\d:\\d\\d))?");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("#")) field = field.substring(1).trim();
      if (field.length() == 0) return;
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strTime = getOptGroup(match.group(2));
    }
    
    @Override
    public String getFieldNames() {
      return "ID TIME";
    }
  }
  
  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strCall = append(data.strCall, " - ", field.substring(pt+1).trim());
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT);
    if (name.equals("CH")) return new MyChannelField();
    return super.getField(name);
  }
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("yyy/MM/dd hh:mm:ss");
  
  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = SR_PTN.matcher(sAddress).replaceAll("SIDE ROAD");
    sAddress = LI_PTN.matcher(sAddress).replaceAll("LINE");
    return sAddress;
  }
  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b");
  private static final Pattern LI_PTN = Pattern.compile("\\bLI\\b");

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BR", "BRAMPTON",
      "C",  "CALEDON",
      "CA", "CALEDON",
      "MG", "MISSISSAUGA",
      "MI", "MILTON",
      "MO", "MO",
  });
}
