package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


/**
 * Allegan County, MI
 */
public class MIAlleganCountyParser extends DispatchH05Parser {

  public MIAlleganCountyParser() {
    super("ALLEGAN COUNTY", "MI",
          "Date_&_Time:DATETIME! Call_Type:CALL! Priority:PRI! Call_Location:ADDRCITY! Cross_Streets:X! Caller_Name_&_Phone_Number:NAME_PHONE! Units:UNIT! Incident_Number:ID! Narrative:EMPTY! INFO_BLK+ Unit_Times:EMPTY TIMES+");
          setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "incidentnotification@allegancounty.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    return super.getField(name);
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*) (\\(\\d{3}\\) ?\\d{3}-\\d{4})");
  private class MyNamePhoneField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
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
