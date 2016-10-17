package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class TXHuntCountyAParser extends DispatchProQAParser {
    
  public TXHuntCountyAParser() {
    super(CITY_LIST, "HUNT COUNTY", "TX",
          "CALL+? ID! ( UNIT1 CALL CALL+?  ADDR1! ADDR_EXT? Created:TIME1! " +
                     "| ( FIRE_SERVICE PRI CALL | ) ADDR! ( EXTRA/Z CITY UNKNOWN? | CITY UNKNOWN? | EXTRA/Z UNKNOWN | EXTRA? UNKNOWN? ) INFO+ )");
  }
  
  @Override
  public String getFilter() {
    return "smtppagingntxcad@emsc.net,logissmtp@emsc.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    String city = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (city != null) data.strCity = city;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT1")) return new UnitField("[^ ]+");
    if (name.equals("ADDR1")) return new MyAddressField();
    if (name.equals("ADDR_EXT")) return new MyAddressExt();
    if (name.equals("TIME1")) return new MyTime1Field();
    
    if (name.equals("FIRE_SERVICE")) return new SkipField("Fire Service", true);
    if (name.equals("PRI")) return new PriorityField("(\\d)-.*", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("UNKNOWN")) return new  SkipField("<Unknown>", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_ADDR_PTN1 = Pattern.compile("(.*)\\bAddr: *(.*)");
  private static final Pattern ROUTE_DASH_PTN = Pattern.compile("\\b(CR|FM|PR|ST|TX|US)-(?=\\d)", Pattern.CASE_INSENSITIVE);
  private static final Pattern PR_PTN = Pattern.compile("\\bPR\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*), *([A-Za-z ]+)(?: \\d{5}\\b| \\(unknown\\)|, *TX\\b(?: +\\d{5}\\b)?) *(.*)");
  private static final Pattern ADDR_X_PTN = Pattern.compile("(.*?)[ ,]*(\\bX[- ]+)(.*)", Pattern.CASE_INSENSITIVE);
  
  boolean addrExt = false;
  
  private class MyAddressField extends MyCallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      addrExt = false;

      // See if we can identify the Addr: keyword.  If it isn't there
      // this is an absolute fail
      Matcher match = CALL_ADDR_PTN1.matcher(field);
      if (!match.matches()) return false;
      String call = match.group(1).trim();
      String addr = match.group(2);
      
      // Next look for a city/zip construct
      // Not finding it is a failure, unless we are processing the final
      // fallback type 3 field
      String trailer;
      boolean noCity = false;
      match = ADDR_CITY_PTN.matcher(addr);
      if (!match.matches()) {
        String addr2 = append(addr, " / ", getRelativeField(+1));
        match = ADDR_CITY_PTN.matcher(addr2);
        if (match.matches()) {
          addrExt = true;
        } else {
          noCity = true;
        }
        
      }
      if (!noCity) {
        addr = match.group(1);
        data.strCity = match.group(2).trim();
        trailer = match.group(3);
      }
      else {
        trailer = "";
      }
      
      // We are good to go.  go ahead and parse the call portion of this field
      super.parse(call, data);
      
      // Parsing the address portion is going to get messy
      // Fix the route prefix dash number constructs
      addr = ROUTE_DASH_PTN.matcher(addr).replaceAll("$1 ");
      
      // Change PR to PVT
      addr = PR_PTN.matcher(addr).replaceAll("Pvt");
      
      // Work on portion ahead of city field
      // which needs to be split into place, cross and address
      
      // So many different branches.  Is there a cross street indicator
      StartType st;
      match = ADDR_X_PTN.matcher(addr);
      if (match.matches()) {
        
        // Yes we have a cross street indicator  Anything in front of it goes
        // in place field
        data.strPlace = append(data.strPlace, " - ", match.group(1));
        addr = match.group(3).trim();
        
        // If the cross street indicator ended with a double blank, there really
        // is no cross street, everthing goes in the address
        if (match.group(2).endsWith("  ")) {
          st = StartType.START_ADDR;
        } else {
          
          // Otherwise, look for a double blank that separates the cross street
          // from the addrss
          int pt = addr.indexOf("  ");
          if (pt >= 0) {
            st = StartType.START_ADDR;
            data.strCross = append(data.strCross, " / ", addr.substring(0,pt));
            addr = addr.substring(pt+2).trim();
          } 
          
          // No double blank.
          else {
            
            // Try to use the address parser to parse a leading cross street
            Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, addr);
            if (res.isValid()) {
              st = StartType.START_ADDR;
              res.getData(data);
              addr = res.getLeft();
            } 
            
            // If that did work, use the main address parser call to identify a leading
            // cross street (that does not look like a cross street, so it probably will
            // not confuse the address parser
            else {
              st = StartType.START_OTHER;
            }
          }
        }
      }
      
      // No cross street marker
      else {
        
        // Is there a double blank delimiter
        // If there is, use it to mark the end of the place and start of the address
        int pt = addr.indexOf("  ");
        if (pt >= 0) {
          
          st = StartType.START_ADDR;
          data.strPlace = append(data.strPlace, " - ", addr.substring(0,pt));
          addr = addr.substring(pt+2).trim();
        }

        // Otherwise count on the address parser to split them apart
        else {
          st = StartType.START_PLACE;
        }
      }
      
      // We have taken the parsing of the first part as far as we can go without
      // invoking the address parser.  Now call it to finish things up
      Result res = parseAddress(st, FLAG_ANCHOR_END, addr);
      if (res.getStatus() == STATUS_NOTHING) {
        parseAddress(addr, data);
      } 
      else {
        res.getData(data);
        if (st == StartType.START_OTHER) {
          data.strCross = append(data.strCross, " / ", res.getStart());
        }
      }
      
      // Split up the trailer following the city/zip code
      if (trailer.length() > 0) {
        
        // It needs the same route prefix fixes as the address does
        trailer = ROUTE_DASH_PTN.matcher(trailer).replaceAll("$1 ");
        trailer = PR_PTN.matcher(trailer).replaceAll("Pvt");
        
        match = ADDR_X_PTN.matcher(trailer);
        if (match.matches()) {
          data.strPlace = append(data.strPlace, " - ", match.group(1));
          data.strCross = append(data.strCross, " / ", match.group(3));
        }
        else if (!trailer.equalsIgnoreCase("X")) {
          data.strPlace = append(data.strPlace, " - ", trailer);
        }
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE X ADDR APT CITY";
    }
  }
  
  private class MyAddressExt extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return addrExt;
    }
  }
  
  private DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyTime1Field extends TimeField {
    
    public MyTime1Field() {
      super("\\d\\d?:\\d\\d [AP]M", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      setTime(TIME_FMT, field, data);
    }
  }
  
  private static final Pattern CALL_CODE_PTN1 = Pattern.compile("(.*) - +(\\d+-[A-Z]-\\d+)");
  private static final Pattern CALL_CODE_PTN2 = Pattern.compile("(\\d\\d?[A-Z]\\d\\d?[A-Z]?) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN1.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      else if ((match = CALL_CODE_PTN2.matcher(field)).matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  private static final Pattern EXTRA_RESID = Pattern.compile("RESID *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern EXTRA_X_PTN = Pattern.compile("(?:X|CROSS)[ -]+(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern EXTRA_APT_PTN = Pattern.compile("APT +([^ ]+) *(.*)|LOT .*", Pattern.CASE_INSENSITIVE);
  private static final Pattern EXTRA_X2_PTN = Pattern.compile("(?:NORTH|SOUTH|EAST|WEST) OF .*|BETWEEN .*|CORD .*", Pattern.CASE_INSENSITIVE);
  private class MyExtraField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return checkParse(field, data, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      checkParse(field, data, false);
    }
      
    public boolean checkParse(String field, Data data, boolean optional) {
        
      if (field.equals("<Unknown>") || field.equals("NO CROSS STREET") ||
          field.equals("NO X STREET") || field.equals("NO X GIVEN") ||
          field.equalsIgnoreCase("X")) return true;
      
      boolean status = !optional;
      Matcher match = EXTRA_RESID.matcher(field);
      if (match.matches()) {
        status = true;
        field = match.group(1);
      }
      
      match = EXTRA_X_PTN.matcher(field);
      if (match.matches()) {
        data.strCross = match.group(1);
        return true;
      }
      
      match = EXTRA_X2_PTN.matcher(field);
      if (match.matches()) {
        data.strCross = field;
        return true;
      }
      
      if (isValidAddress(field)) {
        data.strCross = field;
        return true;
      }
      
      match = EXTRA_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        if (data.strApt != null) {
          status = true;
          field = match.group(2);
        } else {
          data.strApt = field;
          return true;
        }
      }
      
      if (!status) return false;
      
      data.strPlace = field;
      return true;
    }

    @Override
    public String getFieldNames() {
      return "X APT PLACE";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PRIVATE_PTN.matcher(addr).replaceAll("PVT");
    addr = PRNNN_PTN.matcher(addr).replaceAll("PVT ROAD $1");
    return addr;
  }
  private static final Pattern PRIVATE_PTN = Pattern.compile("\\bPRIVATE\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PRNNN_PTN = Pattern.compile("\\bPR *(\\d+)\\b", Pattern.CASE_INSENSITIVE);
  
  private static final String[] CITY_LIST = new String[]{
    "CADDO MILLS",
    "CAMPBELL",
    "CASH",
    "CELESTE",
    "COMMERCE",
    "FLOYD",
    "GREENVILLE",
    "HAWK COVE",
    "LONE OAK",
    "MERIT",
    "NEYLANDVILLE",
    "QUILAN",  // typo
    "QUINLAN",
    "ROYCE CITY",  // Typo
    "ROYSE CITY",
    "TAWAKONI",
    "UNION VALLEY",
    "WEST TAWAKONI",
    "WOLFE CITY",
    
    // Kaufman County
    "TERRELL",
    
    // Van Zandt County
    "WILLS POINT",
    "WILLIS POINT"  // typo
  };
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "QUILAN",        "QUINLAN",
      "ROYCE CITY",    "ROYSE CITY",
      "WILLIS POINT",  "WILLS POINT"
  });
}
