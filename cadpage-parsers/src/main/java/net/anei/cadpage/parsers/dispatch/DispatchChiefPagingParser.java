package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Kent County, DE
 */
public class DispatchChiefPagingParser extends FieldProgramParser {
  
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("[ -]+"); 
  private static final Pattern X_ALT_KEYWORD_PTN = Pattern.compile("\\b(?:Cross STS|Cross Sts|XST|XSts):");
  private static final Pattern DELIM = Pattern.compile(" -- | : | - (?=Xst's:|Caller:)| (?=Lat:|Long:)");
  
  public DispatchChiefPagingParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "CALL ADDRCITY! Xst's:X Caller:NAME? Lat:GPS1 Long:GPS2");
  }
  
  @Override
  public String getFilter() {
    return "@cfmsg.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String[]  subFlds = subject.split("\\|");
    if (subFlds.length > 2) return false;
    if (!subFlds[0].equals("Chief Alert") && !subFlds[0].equals("Chief ALT")) return false;
    Matcher match = LEAD_JUNK_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()); 
    if (subFlds.length > 1) data.strSource = subFlds[1].trim();
    
    // remove any line breaks
    body = body.replace("\n", "");
    
    // They have a variety of cross street keywords that we will map to the
    // most common Xst's:
    body = X_ALT_KEYWORD_PTN.matcher(body).replaceFirst("Xst's:");
    
    if (!super.parseFields(DELIM.split(body), data)) return false;
    if (data.strName.equals("N/A")) data.strName = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  // Call field concatenates with dashes
  // And extract possible leading code
  private static final Pattern CODE_PTN = Pattern.compile("^(\\d[A-Z0-9]*) *- *", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  // Address field is going to be complicated.  It may process two fields.  This
  // one and the one following.  When it does it has to make the determination
  // which one looks more like an address field and treat the other as a place 
  // field
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(?:([A-Z]{2}) +)?\\d{5}");
  private static final Pattern ADDR_ST_PTN = Pattern.compile("(.*) (DE|MD|NJ)");
  private static final Pattern ZIP_PTN = Pattern.compile("(.*) \\d{5}");
  private static final Pattern SPECIAL_ADDR_PTN = Pattern.compile("70 HQ|EASTER SEALS", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = fixAddress(field);
      ExtResult res1 = new ExtResult(field);
      String fld2 = getRelativeField(+1);
      if (fld2.length() > 0 && !fld2.startsWith("Xst's:") && !fld2.startsWith("Caller:")) {
        fld2 = fixAddress(fld2);
        ExtResult res2 = new ExtResult(fld2);
        if (res2.getStatus() > res1.getStatus()) {
          res1 = res2;
          data.strPlace = field;
        }
        else if (res2.hasCity()) {
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, fld2, data);
          data.strPlace = stripFieldEnd(getStart(), "-");
        }
        else {
          data.strPlace = fld2;
        }
      }
      res1.getData(data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST PLACE";
    }
    
    private String fixAddress(String addr) {
      addr = addr.replace('@', '&');
      addr = FOUR_DIGIT_ZIP_PTN.matcher(addr).replaceAll("0$0");
      return addr;
    }
  }
  private static final Pattern FOUR_DIGIT_ZIP_PTN = Pattern.compile("\\b8070\\b");
  
  /**
   * This class extends the Result class with additional information we
   * need to properly parse and check our special address constructs
   */
  private class ExtResult {
    private Result result = null;
    private int status = 0;
    String city = null;
    String state = null;
    
    public ExtResult(String field) {
      
      // There is (almost) always a comma delimited city/state field
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0,pt);
        
        // Sometimes the trailing field is a state zip field, in which case
        // the city will be found in the address line
        // Or just a city field, in  which case state may be found at end
        // of address line
        Matcher match = ADDR_ST_ZIP_PTN.matcher(city);
        if (match.matches()) {
          city = null;
          state = match.group(1);
          if (state == null) {
            match = ADDR_ST_PTN.matcher(field);
            if (match.matches()) {
              field = match.group(1).trim();
              state = match.group(2);
            }
          }
          parseAddress(FLAG_RECHECK_APT, field);
          status = result.getCity().length() == 0 ? 0 : Integer.MAX_VALUE;
          return;
        }
        
        // Otherwise, the trailing item should be real city and we have to infer the state
        if (!isCity(city)) return;
        status = Integer.MAX_VALUE;
        parseAddress(FLAG_NO_CITY | FLAG_RECHECK_APT, field);
        return;
      }
      
      // OK, sometimes there is no comma, in which case we expect the smart
      // address parser to find it at the end of the field possibly followed
      // by a zip code
      Matcher match = ZIP_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();
      
      match = ADDR_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        state = match.group(2);
      }
      
      parseAddress(FLAG_CHECK_STATUS | FLAG_RECHECK_APT, field);
      status = result.getStatus();
      if (status > 0) status++;
      else if (result.getCity().length() > 0 ||
               SPECIAL_ADDR_PTN.matcher(field).matches()) status = 1;
    }
    
    private void parseAddress(int flags, String field) {
      if (field.startsWith("<unknown>")) {
        city = field.substring(9).trim();
        field = "<unknown>";
      }
      result = DispatchChiefPagingParser.this.parseAddress(StartType.START_ADDR, flags | FLAG_ANCHOR_END, field);
    }
    
    public int getStatus() {
      return status;
    }
    
    public boolean hasCity() {
      if (status == 0) return false;
      return result.getCity().length() > 0;
    }
    
    public void getData(Data data) {
      if (result != null) result.getData(data);
      if (city != null) data.strCity = city;
      if (state != null) data.strState = state;
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = field.replace(" Cross St2: ", " / ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*) (\\d{10}|\\d{7})");
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }
}
