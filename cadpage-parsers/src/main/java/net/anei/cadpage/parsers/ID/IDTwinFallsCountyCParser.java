package net.anei.cadpage.parsers.ID;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDTwinFallsCountyCParser extends FieldProgramParser {

  public IDTwinFallsCountyCParser() {
    this("TWIN FALLS COUNTY", "ID");
  }

  public IDTwinFallsCountyCParser(String defCity, String defState) {
    super(defCity, defState,
          "UNIT ID CALL ADDRCITYST PLACE INFO! UNIT EMPTY END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "cad@sircomm.com";
  }

  @Override
  public String getAliasCode() {
    return "IDTwinFallsCountyC";
  }

  private Set<String> unitSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" -- -- ", " --  -- ");
    if (body.endsWith(" --")) body += ' ';
    unitSet.clear();
    return parseFields(body.split(" -- "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID"))  return new IdField("CFS\\d{6}-\\d{4}", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      StringBuilder sb = new StringBuilder(data.strUnit);
      for (String unit : field.split(";")) {
        unit = unit.trim();
        if (unitSet.add(unit)) {
          if (sb.length() > 0) sb.append(',');
          sb.append(unit);
        }
      }
      data.strUnit = sb.toString();
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^| *; )\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - LOG - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INN_PTN = Pattern.compile("\\b(I)[- ]+(\\d{1,3})\\b");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return INN_PTN.matcher(address).replaceAll("$1$2");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "MM 174 I84",                           "+42.636194,-114.428057",
      "MM 175 I84",                           "+42.629976,-114.410043",
      "MM 176 I84",                           "+42.623665,-114.392742",
      "MM 177 I84",                           "+42.617337,-114.375063",
      "MM 178 I84",                           "+42.610130,-114.357880",
      "MM 179 I84",                           "+42.601244,-114.342321",
      "MM 180 I84",                           "+42.592462,-114.326997",
      "MM 181 I84",                           "+42.583596,-114.311528",
      "MM 182 I84",                           "+42.577167,-114.294955",
      "MM 183 I84",                           "+42.576573,-114.275148",
      "MM 184 I84",                           "+42.576391,-114.255487",
      "MM 185 I84",                           "+42.576332,-114.236203",
      "MM 186 I84",                           "+42.576217,-114.216200",
      "MM 187 I84",                           "+42.576152,-114.196961",
      "MM 188 I84",                           "+42.575909,-114.177364",
      "MM 189 I84",                           "+42.576189,-114.157837",
      "MM 190 I84",                           "+42.574721,-114.138574",
      "MM 191 I84",                           "+42.575899,-114.118415",
      "MM 192 I84",                           "+42.576544,-114.099486",
      "MM 193 I84",                           "+42.575948,-114.080030",
      "MM 194 I84",                           "+42.576264,-114.059696",
      "MM 195 I84",                           "+42.575986,-114.041187",
      "MM 196 I84",                           "+42.576107,-114.021142",
      "MM 197 I84",                           "+42.576082,-114.001414",
      "MM 198 I84",                           "+42.576167,-114.981854",
      "MM 199 I84",                           "+42.576241,-114.962423",
      "MM 200 I84",                           "+42.576349,-113.943082",
      "MM 221 HWY 30",                        "+42.548249,-114.414734",
      "MM 222 HWY 30",                        "+42.548199,-114.395128",
      "MM 223 HWY 30",                        "+42.548122,-114.375534",
      "MM 224 HWY 30",                        "+42.540982,-114.364746",
      "MM 225 HWY 30",                        "+42.533787,-114.350850",
      "MM 226 HWY 30",                        "+42.533203,-114.331233",
      "MM 227 HWY 30",                        "+42.533114,-114.311908",
      "MM 228 HWY 30",                        "+42.533281,-114.292196",
      "MM 229 HWY 30",                        "+42.532928,-114.272776",
      "MM 230 HWY 30",                        "+42.533045,-114.252873",
      "MM 231 HWY 30",                        "+42.533224,-114.233356",
      "MM 232 HWY 30",                        "+42.532943,-114.233452",
      "MM 233 HWY 30",                        "+42.516272,-114.207502",
      "MM 234 HWY 30",                        "+42.502995,-114.199056",
      "MM 235 HWY 30",                        "+42.490147,-114.190422",
      "MM 236 HWY 30",                        "+42.477513,-114.182082",
      "MM 237 HWY 30",                        "+42.474825,-114.163637",
      "MM 238 HWY 30",                        "+42.474994,-114.144234",
      "MM 239 HWY 30",                        "+42.474659,-114.124928",
      "MM 240 HWY 30",                        "+42.475061,-114.105032",
      "MM 241 HWY 30",                        "+42.474976,-114.085630",
      "MM 242 HWY 30",                        "+42.474930,-114.066055",
      "MM 0 HWY 50",                          "+42.548109,-114.364842",
      "MM 1 HWY 50",                          "+42.547984,-114.345262",
      "MM 2 HWY 50",                          "+42.548271,-114.325752"
  });
}
