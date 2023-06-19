package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHAuglaizeCountyAParser extends FieldProgramParser {

  public OHAuglaizeCountyAParser() {
    super(CITY_CODES, "AUGLAIZE COUNTY", "OH",
          "SIG:CODE! NAME:NAME! LOC:ADDR/S! CROSS:X! DESC:INFO! CFS:ID! DATE:DATE! TIME:TIME! CITY:CITY! INFO/N+ SECTOR:MAP! DISTRICT:MAP! SIG_DESC:CALL! END");
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?=TIME:|DISTRICT:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  private static Pattern REDUNDANT_X = Pattern.compile("(.*?) *& *(\\1)");
  private static final Pattern WOPAK_FISCHER_RD_PTN = Pattern.compile("\\bWAPAK FISHER RD\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    if (cross) {
      Matcher mat = REDUNDANT_X.matcher(sAddress);
      if (mat.matches()) sAddress = mat.group(1);
    }
    sAddress = WOPAK_FISCHER_RD_PTN.matcher(sAddress).replaceAll("WAPAKONETA FISHER RD");
    return super.adjustMapAddress(sAddress);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{6}", true);
    if (name.equals("DATE")) return new DateField("\\d{1,2}/\\d{1,2}/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("MAP")) return new MapField("\\d+[A-Z]+");
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("(.*) (?:UNIT|APT) +(\\S+)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      // Strip out apt
      String apt = "";
      Matcher match = ADDR_UNIT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }

      // road@road to road & road
      super.parse(field.replace("@", " & "), data);

      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyCrossField extends CrossField {
    public MyCrossField() {
      super("&? *(.*?) *&?", true);
    }

    @Override
    public void parse(String field, Data data) {

      // Strip out city codes
      String saveCity = data.strCity;
      StringBuilder sb = new StringBuilder();
      for (String part : field.split("&")) {
        part = part.trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_ONLY_CITY | FLAG_ANCHOR_END, part, data);
        if (data.strCross.length() > 0) {
          if (sb.length() > 0) sb.append(" & ");
          sb.append(data.strCross);
        }
      }
      data.strCross = sb.toString();
      if (saveCity.length() > 0) data.strCity = saveCity;

      // consolidate entries like "US 33 & US 33"
      Matcher mat = REDUNDANT_X.matcher(data.strCross);
      if (mat.matches()) data.strCross = mat.group(1);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      // if info starts with call, remove it
      if (data.strSupp.startsWith(data.strCall)) {
        data.strSupp = data.strSupp.substring(data.strCall.length()).trim();
      }
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CV", "CRIDERSVILLE",
      "FR", "WAPAKONETA",
      "MN", "MINSTER",
      "NH", "NEW HAMPSHIRE",
      "SJ", "ST JOHNS",
      "SM", "ST MARYS",
      "UN", "UNIOPOLIS",
      "WA", "WAPAKONETA",
      "WF", "WAYNESFIELD",

      "ALLEN CO",    "ALLEN COUNTY",
      "HARDIN CO",   "HARDIN COUNTY",
      "LOGAN CO",    "LOGAN COUNTY",
      "MERCER CO",   "MERCER COUNTY",
      "SHELBY CO",   "SHELBY COUNTY",
      "VAN WERT CO", "VAN WERT COUNTY"
  });
}
