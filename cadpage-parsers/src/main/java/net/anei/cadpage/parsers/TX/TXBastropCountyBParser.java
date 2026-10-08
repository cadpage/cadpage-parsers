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
  private static final Pattern ZIP_CITY_PTN = Pattern.compile("\\d{5} +(.*)");

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
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        data.strSource = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
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
