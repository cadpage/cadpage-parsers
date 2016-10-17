package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Allegan County, MI
 */
public class MIAlleganCountyParser extends DispatchA9Parser {
  
  public MIAlleganCountyParser() {
    super(null, "ALLEGAN COUNTY", "MI");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "@allegancounty.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern MNN_HWY_PTN1 = Pattern.compile("\\bM ?(\\d+) HWY");
  private static final Pattern MNN_HWY_PTN2 = Pattern.compile("\\bM_(\\d+) HWY");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MNN_HWY_PTN1.matcher(field).replaceAll("M_$1 HWY");
      super.parse(field, data);
      data.strAddress = MNN_HWY_PTN2.matcher(data.strAddress).replaceAll("M$1 HWY");
    }
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return stripFieldEnd(address, " HWY");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "34 NB I 196", "42.59336,-86.21648",
      "34 SB I 196", "42.59336,-86.21688",
      "35 NB I 196", "42.60745,-86.21864",
      "35 SB I 196", "42.60745,-86.21900",
      "36 NB I 196", "42.62398,-86.21078",
      "36 SB I 196", "42.62398,-86.21128",
      "37 NB I 196", "42.63197,-86.19904",
      "37 SB I 196", "42.63197,-86.19955",
      "38 NB I 196", "42.64425,-86.18860",
      "38 SB I 196", "42.64425,-86.18897",
      "39 NB I 196", "42.65541,-86.18390",
      "39 SB I 196", "42.65541,-86.18426",
      "40 NB I 196", "42.66707,-86.17190",
      "40 SB I 196", "42.66707,-86.17292",
      "41 NB I 196", "42.68180,-86.16984",
      "41 SB I 196", "42.68180,-86.17020",
  });
  
}
