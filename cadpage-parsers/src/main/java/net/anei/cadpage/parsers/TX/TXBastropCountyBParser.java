package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBastropCountyBParser extends HtmlProgramParser {

  public TXBastropCountyBParser() {
    super("BASTROP COUNTY", "TX",
          "Location:ADDRCITYST/S6! Coordinates:GPS? Call_Type:CALL! EMD_Code:CODE! Comments:INFO! INFO/N+ Responding_Units:UNIT! Incident_Number:ID! Created_Date/Time:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "sa@logis.dk";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:Update to Incident|New Incident) - \\d+");
  private static final Pattern ZIP_CITY_PTN = Pattern.compile("\\d{5}(?:-\\d{4})? +(.*)");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<meta")) {
      if (!super.parseHtmlMsg(subject, body, data)) return false;
      Matcher match = ZIP_CITY_PTN.matcher(data.strCity);
      if (match.matches()) data.strCity = match.group(1);
      return true;
    } else {
      if (!SUBJECT_PTN.matcher(subject).matches()) return false;
      return parseFields(body.split("\n"), data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {

      // Look for leading place field with regular address following in parentheisis
      // Checking for some special character that proves this is not a alpha source field
      if (field.endsWith(")")) {
        boolean good = false;
        int cnt = 0;
        int pt = field.length()-1;
        for ( ; pt >= 0; pt--) {
          char chr = field.charAt(pt);
          if (chr == ')') cnt++;
          else if (chr == '(') cnt--;
          if (!good) {
            if (cnt > 1 || Character.isDigit(chr) || chr == '/' || chr == '&') good = true;
          }
          if (cnt == 0) break;
        }
        if (cnt > 0) abort();
        if (good) {
          data.strPlace = field.substring(0, pt).trim();
          field = field.substring(pt+1, field.length()-1).trim();
        }
      }

      // OK, now we can look for the trailing source and address fields
      Parser p = new Parser(field);
      String apt = p.getLastOptional(')');
      data.strSource = p.getLastOptional('(');
      field = p.get();
      if (field.isEmpty()) abort();
      super.parse(field, data);
      if (!data.strApt.isEmpty() && data.strAddress.toUpperCase().endsWith("COUNTY LINE RD")) {
        data.strAddress = data.strAddress + ' ' + data.strApt;
        data.strApt = "";
      }
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " SRC";
    }
  }

  private static final Pattern NOT_APT_PTN = Pattern.compile("[A-Z]");

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (NOT_APT_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }
}
