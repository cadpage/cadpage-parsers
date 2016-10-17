package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Baltimore County, MD
 */
public class MDBaltimoreCountyAParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Station (\\d+) ALERT!!(?: \\((\\d+)\\))?");
  
  public MDBaltimoreCountyAParser() {
    super("BALTIMORE COUNTY", "MD",
           "CALL ( UNIT MAP | MAP UNIT ) ADDR/S0! INFO? ID");
  }
  
  @Override
  public String getFilter() {
    return "postmaster@sparkgroup.net,fast@md-carroll-04.fastalerting.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.find()) return false;
    data.strSource = match.group(1);
    data.strCallId = getOptGroup(match.group(2));
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram();
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern MUTUAL_AID_ADDR_PTN = Pattern.compile("(?:(\\d+-\\d+) +)?(.*?)(?: +(OPS\\d+))?");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      if (data.strCall.startsWith("MUTUAL AID")) {
        Matcher match = APT_PTN.matcher(field);
        if (match.find()) {
          data.strApt = append(data.strApt, "-", match.group(1).trim());
          field = field.substring(0,match.start()).trim();
        }
        field = field.replaceAll(" *// *", " ");
        match = MUTUAL_AID_ADDR_PTN.matcher(field);
        if (!match.matches()) abort();   // Can't happen
        String code = match.group(1);
        if (code != null) data.strCall = data.strCall + " " + code;
        field = match.group(2);
        data.strChannel = getOptGroup(match.group(3));
        parseAddress(StartType.START_ADDR, field, data);
        data.strCall = append(data.strCall, " ", getLeft());
      } else {
        Parser p = new Parser(field);
        String city = p.getLastOptional(',');
        if (city.length() == 2) {
          if (!city.equals(data.defState)) data.strState = city;
          city = p.getLastOptional(',');
        }
        
        String extra = p.getLastOptional(';');
        if (extra.length() == 0) extra = p.getLastOptional("   ");
        
        String place = p.getOptional('@');
        String addr = p.get();
        Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, addr);
        if (place.length() > 0) {
          Result res2 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, place);
          if (res2.getStatus() > res.getStatus()) {
            res = res2;
            place = addr;
          }
        }
        res.getData(data);
        data.strPlace = place;
        
        
        if (city.length() > 0) data.strCity = city;
        if (extra.length() > 0) {
          for (String part : extra.split("   +")) {
            Matcher match = APT_PTN.matcher(part);
            if (match.matches()) {
              data.strApt = append(data.strApt, "-", match.group(1).trim());
            } else {
              data.strSupp = append(data.strSupp, " / ", part);
            }
          }
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " APT INFO CITY ST CH";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(".")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("Incident Number \\((\\d+)\\)");
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("MAP")) return new MapField("(?:\\d{3}|CAR)-\\d{2}|\\d{4}|[A-Z]{2}", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
}
