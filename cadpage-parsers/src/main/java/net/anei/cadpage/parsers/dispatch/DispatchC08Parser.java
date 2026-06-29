package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC08Parser extends FieldProgramParser {

  public DispatchC08Parser(String defCity, String defState) {
    super(defCity, defState,
          "Trip_Number:ID! Run_Number:ID/L! Unit:UNIT! Patient_Name:NAME? Nature:CALL! Calltype:CALL/SDS! Level_of_Service:SKIP! Response_Priority:PRI? " +
              "Transport_Priority:SKIP? PU_Date/Time:DATETIME! Pickup_Address:ADDR! Pickup_Rm/Suite:APT? Pickup_City,_State,_Zip:CITY_ST! Pickup_Lat,_Lon:GPS! " +
              "Dropoff_Facility:DPLACE? Dropoff_Address:DADDR? Dropoff_Rm/Suite:DAPT? Dropoff_City,_State,_Zip:DCITY_ST! Dropoff_Lat,_Lon:DGPS? Dispatch_Notes:INFO/N! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active 911: New Call")) return false;
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("CITY_ST")) return new BaseCityStateField();
    if (name.equals("DPLACE")) return new BaseDeliverPlaceField();
    if (name.equals("DADDR")) return new BaseDeliverAddressField();
    if (name.equals("DAPT")) return new BaseDeliverAptField();
    if (name.equals("DCITY_ST"))  return new BaseDeliverCityStateField();
    if (name.equals("DGPS")) return new BaseDeliverGPSField();
    return super.getField(name);
  }

  private class BaseAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NA")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("([^,]*), *([A-Z]{2})(?: +\\d{5}(?:-\\d{4})?)?");
  private class BaseCityStateField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private static final String DELIVER_TO_TAG = "Deliver to:\n";

  private class BaseDeliverPlaceField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      data.strSupp = DELIVER_TO_TAG + field;
    }
  }

  private class BaseDeliverAddressField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (data.strSupp.isEmpty()) data.strSupp = DELIVER_TO_TAG;
      data.strSupp = data.strSupp + '\n' + field;
    }
  }

  private class BaseDeliverAptField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty() || field.equals("NA")) return;
      if (!data.strSupp.isEmpty()) return;
      data.strSupp = data.strSupp + " Apt:" + field;
    }
  }

  private class BaseDeliverCityStateField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strSupp.isEmpty()) return;
      Matcher match = CITY_ST_PTN.matcher(field);
      if (!match.matches()) abort();
      String city = match.group(1).trim();
      if (!city.isEmpty()) {
        data.strSupp = data.strSupp + ", " + city;
      }
      String state = match.group(2);
      if (!state.isEmpty()) {
        data.strSupp = data.strSupp + ", " + state;
      }
    }
  }

  private class BaseDeliverGPSField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (data.strSupp.isEmpty()) {
        data.strSupp = DELIVER_TO_TAG + field;
      } else {
        data.strSupp = data.strSupp + '\n' + field;
      }
    }
  }
}
