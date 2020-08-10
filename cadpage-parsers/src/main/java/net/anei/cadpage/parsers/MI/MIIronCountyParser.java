package net.anei.cadpage.parsers.MI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIIronCountyParser extends DispatchOSSIParser {

  public MIIronCountyParser() {
    this("IRON COUNTY", "MI");
  }

  public MIIronCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "( CANCEL ADDR! INFO+ " +
          "| FYI? CALL MASH+? UNIT! CITY? INFO+ )");
  }

  @Override
  public String getAliasCode() {
    return "MIIronCounty";
  }

  @Override
  public String getFilter() {
    return "CAD@up-911.com";
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(.*)\n(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}[AP]M)");
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("yyyy/MM/dd hh:mm:ssaa");

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (!body.startsWith("CAD:")) body = "CAD:" + body;

    Matcher match = DATE_TIME_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      setDateTime(DATE_TIME_FMT, match.group(2), data);
    }
    if (!super.parseMsg(body, data)) return false;
    return data.strAddress.length() > 0;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MyMashField();
    if (name.equals("CITY")) return new CityField("[ A-Z]+", true);
    if (name.equals("UNIT")) return new UnitField("(?!FS\\d+)(?:[A-Z]?\\d+[A-Z]+|[A-Z]+\\d+|\\d{4}|[A-Z]{2}FD)", true);
    return super.getField(name);
  }

  // General place/address/cross/apt field processor
  private static final Pattern APT_PTN = Pattern.compile("(?:#|APT|LOT|RM|ROOM) *(.*)|\\d{1,4}[A-Z]?");
  private class MyMashField extends Field {

    // Keep internal state so we know where we are
    int state = 0;

    @Override
    public void parse(String field, Data data) {

      // Initialize state for the first field
      if (data.strPlace.length() == 0 && data.strAddress.length() == 0) state = 0;

      // And switch on current state
      switch (state) {

      // Assume first field is address until we have a second to compare it to
      // We do have to save it in the address on the off chance that there is no
      // second field
      case 0:
        parseAddress(field, data);
        state++;
        break;

      // For the second field, we need to do some work to figure out whether the
      // first field is a place name or address.
      case 1:

        // Either way, we will want to increment the state
        state++;

        // Compare the address status of the previous field and this field
        // If this field status is a simple street name, assume it is a
        // following cross street and eliminate it from consideration
        String prevField = getRelativeField(-1);
        int stat1 = checkAddress(prevField);
        int stat2 = checkAddress(field);
        if (stat2 == STATUS_STREET_NAME) stat2 = -1;

        // This field does look like a better address
        // Save the first field in the place name and parse this one
        // as an address
        if (stat2 > stat1) {
          data.strPlace = prevField;
          data.strAddress = "";
          parseAddress(field, data);
          break;
        }

        // Otherwise, address is already parsed so we will fall through
        // to the next case to process this field

      case 2:

        // Anything else is either an apt or a cross street
        Matcher match = APT_PTN.matcher(field);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = field;
          data.strApt = append(data.strApt, "-", apt);
        }

        else {
          data.strCross = append(data.strCross, " / ", field);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR X APT";
    }
  }
}
