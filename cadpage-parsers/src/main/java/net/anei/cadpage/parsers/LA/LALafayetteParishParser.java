package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Lafayette Parish, LA
*/

public class LALafayetteParishParser extends DispatchA49Parser {

  private static final Pattern TRAIL_NA_PTN = Pattern.compile("(?: +OR)? +NA$");

  public LALafayetteParishParser() {
    super("LAFAYETTE PARISH","LA");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("  >", "\n>");
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = TRAIL_NA_PTN.matcher(data.strAddress).replaceFirst("");
    data.strCross = TRAIL_NA_PTN.matcher(data.strCross).replaceFirst("");
    return true;
  }


  @Override
  public String getFilter() {
    return "cadalert@lafayettela.gov,alerts@carencrofd.org";
  }

  @Override
  protected String fixCall(String call) {
    if (call.startsWith("CALL SENT TO ACADIAN IT")) return null;
    if (call.equals("ACADIAN AMBULANCE INCIDENT TRANSFERED")) return null;
    return super.fixCall(call);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)\\.([A-Z])");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = convertCodes(match.group(2), CITY_CODES);
      }
      super.parse(field, data);
    }

    @Override
    public String  getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    sAddress = TW_PTN.matcher(sAddress).replaceAll("THRUWAY");
    if (cross) {
      Matcher match = CROSS_HOUSE_PTN.matcher(sAddress);
      if (match.matches()) sAddress = match.group(1);
    }
    return super.adjustMapAddress(sAddress, cross);
  }
  private static final Pattern TW_PTN = Pattern.compile("\\bTW\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CROSS_HOUSE_PTN = Pattern.compile("\\d+ +(.*)");

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "B", "BROUSSARD",
      "C", "CARENCRO",
      "D", "DUSON",
      "L", "LAFAYETTE",
      "P", "LAFAYETTE PARISH",
      "S", "SCOTT",
      "Y", "YOUNGSVILLE"

  });
}
