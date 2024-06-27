package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NCForsythCountyAParser extends FieldProgramParser {
  
  public NCForsythCountyAParser() {
    super(CITY_CODES, "FORSYTH COUNTY", "NC",
           "Location:ADDR/S Nature:CALL! P:PRI DISTRICT:UNIT X_Str:X CALLER_NAME:NAME");
  }
  
  @Override
  public String getFilter() {
    return "forsythcountyfir@bellsouth.net,lfdalltext@lewisvillefire.com,gfdpaging@griffithfire.com,pgfr32@triad.rr.com,fces@triad.twcbc.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(": @", " @").replace('_', ' ');
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strCity.equals(data.defCity)) data.strCity = "";
    if (data.strCross.endsWith("/")) data.strCross = data.strCross.substring(0,data.strCross.length()-1).trim();
    
    // Intersections seem to be saved as cross streets, at least some of the time
    if (data.strAddress.length() == 0) {
      if (data.strCross.length() == 0) return false;
      String sAddress = data.strCross;
      data.strCross = "";
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, sAddress, data);
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ADDR";
  }
  
  
  private static final Pattern MA_PATTERN = Pattern.compile("@([A-Z ]+):"); 
  private static final Pattern MA_PATTERN2 = Pattern.compile("^\\d+ +([A-Z ]+ CO(?:UNTY)?):");
  private static final Pattern APT_PTN = Pattern.compile("[,:](?:APT|RM|(?! )) *(.*?)$");
  private static final Pattern FC_PTN = Pattern.compile("\\bFC\\b");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String fld, Data data) {
      Matcher match = MA_PATTERN.matcher(fld);
      boolean found = match.find();
      if (!found) {
        match = MA_PATTERN2.matcher(fld);
        found = match.find();
      }
      if (found) {
        data.strCity = match.group(1);
        if (data.strCity.endsWith(" CO")) data.strCity = data.strCity + "UNTY";
        fld = fld.substring(match.end()).trim();
      } 
      int pt = fld.indexOf('@');
      if (pt >= 0) {
        data.strPlace = fld.substring(pt+1).trim();
        fld = fld.substring(0,pt).trim();
      }
      
      match = APT_PTN.matcher(fld);
      if (match.find()) {
        data.strApt = match.group(1);
        fld = fld.substring(0,match.start()).trim();
      }
      
      super.parse(fld, data);
      
      // The use FC (Forsythe County) to indicate county roads,
      // which neither the smart parser nor Google understand.
      // so change them to CO. (we can't do this prior to the smart parse
      // call because 'FC' is a legitimate city code.
      data.strAddress = FC_PTN.matcher(data.strAddress).replaceAll("CO");
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0,pt);
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return RA_PTN.matcher(addr).replaceAll("");
  }
  private static final Pattern RA_PTN = Pattern.compile("\\bRA\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BE",   "BETHANIA",
      "BELE", "BELEWS CREEK",
      "BETH", "BETHANIA",
      "CL",   "CLEMMONS",
      "CLEM", "CLEMMONS",
      "COLF", "COLFAX",
      "DC",   "DAVIDSON COUNTY",
      "EDEN", "EDEN",
      "FC",   "FORSYTH COUNTY",
      "GC",   "GUILFORD COUNTY",
      "GER",  "GERMANTON",
      "GERM", "GERMANTON",
      "HP",   "HIGH POINT",
      "KE",   "KERNERSVILLE",
      "KER",  "KERNERSVILLE",
      "KI",   "KING",
      "KING", "KING",
      "LE",   "LEWISVILLE",
      "LEW",  "LEWISVILLE",
      "PFAF", "PFAFFTOWN",
      "RH",   "RURAL HALL",
      "SC",   "STOKES COUNTY",
      "STAN", "STANLEYVILLE",
      "STOK", "STOKESDALE",
      "SU",   "SURRY",
      "TO",   "TOBACCOVILLE",
      "TOB",  "TOBACCOVILLE",
      "TOBA", "TOBACCOVILLE",
      "WA",   "WALKERTOWN",
      "WALK", "WALKERTOWN",
      "WALN", "WALNUT COVE",
      "WS",   "WINSTON-SALEM",

  });
}
