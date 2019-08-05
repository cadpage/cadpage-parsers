package net.anei.cadpage.parsers.NC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCStanlyCountyParser extends DispatchOSSIParser {
  
  private List<String> addressList = new ArrayList<String>();
  
  public NCStanlyCountyParser() {
    super(CITY_CODES, "STANLY COUNTY", "NC",
           "FYI? CALL ( SELECT/1 ADDR1/Z+? CITY! | CALL2+? ADDR! ADDR2? ) ( X | PLACE X | ) X+? INFO+");
    setupCityValues(CITY_CODES);
    setDelimiter('/');
    addRoadSuffixTerms("CONNECTOR");
  }
  
  @Override
  public String getFilter() {
    return "CAD@sclg.gov,CAD@stanlycountync.gov";
  }
  
  private static final Pattern BAD_MSG_PTN = Pattern.compile("CAD:BE ADVISED.*|.*\\bSCC/ ?[A-Z]+|.*\\bauth / \\S+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
  
  @Override
  public boolean parseMsg(String body, Data data) {
    boolean good = body.startsWith("CAD:");
    if (!good) body = "CAD:" + body;
    addressList.clear();
    if (body.contains("; ")) body = body.replace(';', '/');
    setSelectValue("1");
    if (super.parseMsg(body, data)) return true;
    
    // Primary parse algorithm only works if there is a city field
    // which occasionally there is not in which case try the secondary 
    // parse algorithm
    if (!good) return false;
    if (BAD_MSG_PTN.matcher(body).matches()) return false;
    setSelectValue("2");
    data.strCall = "";
    return super.parseMsg(body,  data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  // Things get complicated here, the address field just accumulates fields
  // until we find a city field
  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String fld, Data data) {
      addressList.add(fld);
    }
  }
  
  // The city list is where we decide what to do with the address fields
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String fld, Data data) {
      if (!super.checkParse(fld, data)) return false;
      
      // OK, we found address, now figure out what to do with the address
      // If there are more than one address field and the last one is a simple
      // naked road, then merge the last two fields together to form the
      // address field.  If not, the last field is the only address field
      int addrNdx = addressList.size()-1;
      if (addrNdx < 0) abort();
      String sAddr = addressList.get(addrNdx);
      if (addrNdx > 0 && isStreetName(sAddr, true)) {
        sAddr = addressList.get(--addrNdx) + " & " + sAddr;
      }
      parseAddress(sAddr, data);
      
      // Any fields in front of the address field will be appended to the
      // call description
      for (int ii = 0; ii<addrNdx; ii++) {
        data.strCall = append(data.strCall, "/", addressList.get(ii));
      }
      return true;
    }
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL_SET.contains(field)) return false;
      data.strCall = append(data.strCall, "/", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isStreetName(field, true)) return false;
      data.strAddress = append(data.strAddress, " & ", field);
      return true;
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isStreetName(field, false)) return false;
      parse(field, data);
      return true;
    }
  }
  
  private boolean isStreetName(String field, boolean strict) {
    int stat = checkAddress(field);
    return  stat == STATUS_STREET_NAME ||
            !strict && stat == STATUS_INTERSECTION || 
            field.contains("BUSINESS 52") ||
            field.equals("COUNTY LINE");
  }
  
  private static final Set<String> CALL_SET = new HashSet<String>(Arrays.asList(new String[]{
      "ATTACKS",
      "CHILDBIRTH",
      "COLD EXPOSURE",
      "CONVULSION",
      "CVA",
      "DIVING ACCIDENT",
      "EXPLOSION",
      "FAINTING",
      "GUNSHOT WOUND",
      "HIVES",
      "INGESTION",
      "LACERATIONS",
      "MACHINE ACCIDEN",
      "MAN DOWN",
      "MED REACT",
      "MISCA",
      "MOTORCYCL",
      "POISONI",
      "PROBLEM",
      "PROBLEMS",
      "RAPE",
      "RESPIRATORY ARREST",
      "STIN",
      "SUICIDE ATTEMPT"
}));
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALB",  "ALBEMARLE",
      "BAD",  "BADIN",
      "GLH",  "GOLD HILL",
      "LOC",  "LOCUST",
      "MID",  "MIDLAND",
      "MIS",  "MISENHEIMER",
      "MTP",  "MT PLEASANT",
      "NEW",  "NEW LONDON",
      "NOR",  "NORWOOD",
      "OAK",  "OAKBORO",
      "RFD",  "RICHFIELD",
      "SFD",  "STANFIELD"
  });
}
