package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNiagaraCountyAParser extends FieldProgramParser {
  
  private String delim;

  public NYNiagaraCountyAParser() {
    super(CITY_LIST, "NIAGARA COUNTY", "NY", 
          "CALL? ADDR! INFO+");
    removeWords("CORD", "UNIT");
  }
  
  @Override
  public String getFilter() {
    return "@niagaracounty.com,777";
  }

  private static final Pattern SRC_SUBJECT_PTN = Pattern.compile("TCAS|Station \\d+");
  private static final Pattern ID_SUBJECT_PTN = Pattern.compile("\\((\\d+)\\) NCFC [A-Z]+");
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile("[ @]+(?:(\\d\\d):?(\\d\\d)(?: ?HRS)?|(\\d:\\d{2}))$", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_DATE_PTN = Pattern.compile(" +(\\d{1,2}/\\d{1,2}(?:/\\d{2}(?:\\d{2})?)?)$");
  private static final Pattern LEAD_DATE_TIME_PTN = Pattern.compile("(\\d{2})(\\d{2})HRS ON (\\d{2})(\\d{2})(\\d{2})[- ]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_OPS_PTN = Pattern.compile("[- ]+(OPS *\\d*)$", Pattern.CASE_INSENSITIVE);
  private static final Pattern DASH_DELIM = Pattern.compile(" +- *|- +|^-");
  private static final Pattern SLASH_DELIM = Pattern.compile("(?<!\\d)/|/(?!\\d)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    do {
      Matcher match = SRC_SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strSource = subject;
        break;
      }
      
      match = ID_SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strCallId = match.group(1);
        break;
      }
      
      return false;
    } while (false);
    
    body = body.replace("\n", "");
    
    // Look for trailing date and time, or time and date
    
    Matcher match = TRAIL_DATE_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.start());
      data.strDate = match.group(1);
    }
    
    match = TRAIL_TIME_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.start());
      data.strTime = match.group(3);
      if (data.strTime == null) {
        data.strTime = match.group(1) + ':' + match.group(2);
      }
      if (data.strDate.length() == 0) {
        match = TRAIL_DATE_PTN.matcher(body);
        if (match.find()) {
          body = body.substring(0,match.start());
          data.strDate = match.group(1);
        }
      }
    }
    
    if (data.strTime.length() == 0) {
      match = LEAD_DATE_TIME_PTN.matcher(body);
      if (match.lookingAt()) {
        data.strTime = match.group(1)+':'+match.group(2);
        data.strDate = match.group(3)+'/'+match.group(4)+'/'+match.group(5);
        body = body.substring(match.end());
      }
    }
    
    match = TRAIL_OPS_PTN.matcher(body);
    if (match.find()) {
      data.strChannel = match.group(1);
      body = body.substring(0,match.start());
    }
    
    // Recent alerts have been using dashes as field delimiters.  See if we can work with that
    // We make two attempts, one looking for blank/dash delimiters, and will go if this only
    // gives us two fields.  If that does not, try unadorned dash delimiters, but we need 3
    // fields to make a go of things
    delim = " - ";
    String[] flds = DASH_DELIM.split(body);
    if (flds.length >= 3 ||
        flds.length >= 2 && flds[0].length() > 0) return parseFields(flds, data);
    
    delim = "-";
    flds = body.split("-", 3);
    if (flds.length < 3) {
      delim = "/";
      flds = SLASH_DELIM.split(body);
    }
    if (flds.length >= 3) return parseFields(flds, data);
    
    // That is about all we can get.  Use SAP for everything else
    setFieldList("CALL ADDR APT CITY INFO");
    parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
    String left = getLeft();
    left = stripFieldStart(left, "-");
    left = stripFieldStart(left, "/");
    if (data.strCall.length() == 0) {
      if (left.length() > 0) {
        data.strCall = left;
      } else {
        data.strCall = data.strAddress;
        data.strAddress = "";
      }
    } else {
      data.strSupp = left;
    }
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID SRC " + super.getProgram() + " CH DATE TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("[ A-Za-z]*", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends MyInfoField {
    @Override
    public void parse(String field, Data data) {
      
      // If this is not the last field, parse a simple address/city
      if (!isLastField()) {
        int flags = FLAG_RECHECK_APT | FLAG_ANCHOR_END;
        Parser p = new Parser(field);
        String city = p.getLastOptional(',');
        if (city.equals("NY")) city = p.getLastOptional(',');
        if (city.length() > 0) {
          data.strCity = city;
          flags |= FLAG_NO_CITY;
        }
        parseAddress(StartType.START_ADDR, flags, p.get(), data);
      }
      
      // Otherwise, leave room for an info field
      else {
        parseAddress(StartType.START_ADDR, field, data);
        if (!isValidAddress()) abort();
        
        super.parse(getLeft(), data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY CALL INFO";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0 && data.strSupp.length() == 0 &&
          field.length() <= 40) {
        data.strCall = field;
      } else {
        data.strSupp = append(data.strSupp, delim, field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL INFO";
    }
  }
  
  private static final String[] CITY_LIST = new String[] {

    // Cities
    "LOCKPORT",
    "NIAGARA FALLS",
    "NORTH TONAWANDA",

    // Towns
    "CAMBRIA",
    "HARTLAND",
    "LEWISTON",
    "LOCKPORT",
    "NEWFANE",
    "NIAGARA",
    "PENDLETON",
    "PORTER",
    "ROYALTON",
    "SOMERSET",
    "WHEATFIELD",
    "WILSON",

    // Villages
    "BARKER",
    "LEWISTON",
    "MIDDLEPORT",
    "WILSON",
    "YOUNGSTOWN",

    // Census-designated places
    "GASPORT",
    "NEWFANE",
    "OLCOTT",
    "RANSOMVILLE",
    "RAPIDS",
    "SANBORN",
    "SOUTH LOCKPORT"
    
  };
}
