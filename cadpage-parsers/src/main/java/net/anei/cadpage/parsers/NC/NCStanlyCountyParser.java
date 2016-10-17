package net.anei.cadpage.parsers.NC;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCStanlyCountyParser extends DispatchOSSIParser {
  
  private List<String> addressList = new ArrayList<String>();
  
  public NCStanlyCountyParser() {
    super(CITY_CODES, "STANLY COUNTY", "NC",
           "CALL ADDR/Z+? CITY! ( X | PLACE X | ) X+? INFO+");
    setDelimiter('/');
  }
  
  @Override
  public String getFilter() {
    return "CAD@sclg.gov,CAD@stanlycountync.gov";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    addressList.clear();
    return super.parseMsg(body, data);
  }
  
  // Things get complicated here, the address field just accumulates fields
  // until we find a city field
  private class MyAddressField extends AddressField {
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
      if (addrNdx > 0 && checkAddress(sAddr) == STATUS_STREET_NAME) {
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
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALB", "ALBEMARLE",
      "BAD", "BADIN",
      "GLH", "GOLD HILL",
      "LOC", "LOCUST",
      "MTP", "MT PLEASANT",
      "NEW", "NEW LONDON",
      "NOR", "NORWOOD",
      "OAK", "OAKBORO",
      "RFD", "RICHFIELD",
      "SFD", "STANFIELD"
  });
}
