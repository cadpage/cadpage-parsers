package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStLouisCountyEParser extends FieldProgramParser {

  public MOStLouisCountyEParser() {
    super(MOStLouisCountyParser.CITY_LIST, "ST LOUIS COUNTY", "MO", "Call_Received_Time:DATE_TIME_CALL! ADDR/S! UNIT_PLACE! Description:INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "ECDC@rejis.org,Admin@east-central.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("_Dispatch") && !subject.equals("Dispatch")) return false;

    body = body.replace("Description:", "\nDescription:");

    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_CALL")) return new MyDateTimeCallField();
    if (name.equals("UNIT_PLACE")) return new MyUnitPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_CALL_PATTERN = Pattern.compile(" *(\\d{1,2}/\\d{1,2}/\\d{4}) *(\\d{2}:\\d{2}:\\d{2}) *(.*?)");

  private class MyDateTimeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = DATE_TIME_CALL_PATTERN.matcher(field);
      if (!mat.matches()) abort();
      data.strDate = mat.group(1);
      data.strTime = mat.group(2);
      data.strCall = mat.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }

  private static final Pattern UNIT_PLACE_PATTERN = Pattern.compile("((?:[A-Z\\-]{0,10}\\d{1,10}[ \\-]?)+) ?(?:\\*{2}CHECK BLDG NAME\\*{4})? *(.*?)?");

  private class MyUnitPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = UNIT_PLACE_PATTERN.matcher(field);
      if (mat.matches()) {
        data.strUnit = mat.group(1).trim();
        data.strPlace = getOptGroup(mat.group(2));
      } else data.strUnit = field;
    }

    @Override
    public String getFieldNames() {
      return "UNIT PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\[\\d{1,2}/\\d{1,2}/\\d{4} \\d{2}:\\d{2}:\\d{2} [A-Z]+\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        super.parse(line, data);
      }
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "121 BLUE",                             "+38.648645,-90.314927",
      "123 BLUE",                             "+38.649523,-90.314770",
      "124 BLUE",                             "+38.648021,-90.311244",
      "125 BLUE",                             "+38.649695,-90.310735",
      "127 BLUE",                             "+38.650363,-90.310521",
      "133 BLUE",                             "+38.649468,-90.310538",
      "137 BLUE",                             "+38.649608,-90.309884",
      "138 BLUE",                             "+38.650278,-90.309576",
      "150 BLUE",                             "+38.649782,-90.309919",
      "228 BLUE",                             "+38.648812,-90.312768",
      "250 BLUE",                             "+38.649713,-90.312070",
      "253 BLUE",                             "+38.649670,-90.311434",
      "109 GREEN",                            "+38.649404,-90.305220",
      "110 GREEN",                            "+38.649035,-90.305222",
      "112 GREEN",                            "+38.649163,-90.306825",
      "113 GREEN",                            "+38.649917,-90.307040",
      "116 GREEN",                            "+38.649351,-90.306059",
      "122 GREEN",                            "+38.649105,-90.306176",
      "129 GREEN",                            "+38.649461,-90.306612",
      "135 GREEN",                            "+38.649308,-90.307739",
      "142 GREEN",                            "+38.649795,-90.306364",
      "144 GREEN",                            "+38.649904,-90.307470",
      "195 GREEN",                            "+38.649485,-90.307028",
      "196 GREEN",                            "+38.649607,-90.307663",
      "225 GREEN",                            "+38.649900,-90.307700",
      "254 GREEN",                            "+38.649559,-90.309023",
      "416 GREEN",                            "+38.649984,-90.307883",
      "101 ORANGE",                           "+38.645755,-90.311876",
      "108 ORANGE",                           "+38.647708,-90.308588",
      "130 ORANGE",                           "+38.646362,-90.311217",
      "132 ORANGE",                           "+38.648233,-90.308877",
      "139 ORANGE",                           "+38.647403,-90.307523",
      "140 ORANGE",                           "+38.647262,-90.308601",
      "145 ORANGE",                           "+38.647628,-90.308041",
      "153 ORANGE",                           "+38.648020,-90.309534",
      "154 ORANGE",                           "+38.647549,-90.307012",
      "174 ORANGE",                           "+38.646232,-90.308476",
      "193 ORANGE",                           "+38.647446,-90.309229",
      "194 ORANGE",                           "+38.646296,-90.309914",
      "197 ORANGE",                           "+38.646215,-90.309197",
      "226 ORANGE",                           "+38.647797,-90.310487",
      "240 ORANGE",                           "+38.646504,-90.311470",
      "242 ORANGE",                           "+38.646522,-90.311868",
      "243 ORANGE",                           "+38.647458,-90.308572",
      "247 ORANGE",                           "+38.647179,-90.306593",
      "249 ORANGE",                           "+38.647032,-90.307510",
      "252 ORANGE",                           "+38.646385,-90.311863",
      "199 PURPLE",                           "+38.650579,-90.307245",
      "217 PURPLE",                           "+38.650683,-90.311780",
      "218 PURPLE",                           "+38.650356,-90.312809",
      "219 PURPLE",                           "+38.650774,-90.312217",
      "220 PURPLE",                           "+38.650879,-90.313292",
      "221 PURPLE",                           "+38.650902,-90.312744",
      "224 PURPLE",                           "+38.650059,-90.313199",
      "304 PURPLE",                           "+38.649683,-90.312642",
      "305 PURPLE",                           "+38.649695,-90.312896",
      "306 PURPLE",                           "+38.649760,-90.313162",
      "307 PURPLE",                           "+38.649787,-90.313437",
      "308 PURPLE",                           "+38.649798,-90.313707",
      "311 PURPLE",                           "+38.649810,-90.314045",
      "312 PURPLE",                           "+38.649556,-90.314101",
      "324 PURPLE",                           "+38.651221,-90.314386",
      "326 PURPLE",                           "+38.650587,-90.314049",
      "161 RED",                              "+38.644794,-90.314003",
      "162 RED",                              "+38.645323,-90.314715",
      "163 RED",                              "+38.644875,-90.314568",
      "164 RED",                              "+38.643424,-90.312533",
      "165 RED",                              "+38.643364,-90.312945",
      "166 RED",                              "+38.643506,-90.313326",
      "167 RED",                              "+38.645001,-90.315774",
      "168 RED",                              "+38.645282,-90.315489",
      "169 RED",                              "+38.645059,-90.315161",
      "175 RED",                              "+38.643546,-90.314065",
      "176 RED",                              "+38.643646,-90.314695",
      "177 RED",                              "+38.644296,-90.315884",
      "178 RED",                              "+38.644675,-90.315422",
      "179 RED",                              "+38.643766,-90.315736",
      "246 RED",                              "+38.644179,-90.312229",
      "320 RED",                              "+38.645926,-90.314317",
      "321 RED",                              "+38.646026,-90.312500",
      "322 RED",                              "+38.645575,-90.313205",
      "323 RED",                              "+38.645680,-90.314330",
      "328 RED",                              "+38.643989,-90.313235",
      "330 RED",                              "+38.644955,-90.311788",
      "331 RED",                              "+38.644582,-90.312015",
      "332 RED",                              "+38.644812,-90.313784",
      "333 RED",                              "+38.645059,-90.312812",
      "334 RED",                              "+38.644673,-90.313328",
      "335 RED",                              "+38.644239,-90.313078",
      "336 RED",                              "+38.644365,-90.313577",
      "337 RED",                              "+38.645375,-90.311809",
      "104 SILVER",                           "+38.646537,-90.302064",
      "131 SILVER",                           "+38.646677,-90.303492",
      "151 SILVER",                           "+38.646576,-90.302795",
      "202 SILVER",                           "+38.649145,-90.303142",
      "203 SILVER",                           "+38.647111,-90.302647",
      "204 SILVER",                           "+38.649105,-90.304472",
      "206 SILVER",                           "+38.648956,-90.302248",
      "207 SILVER",                           "+38.648982,-90.301655",
      "410 SILVER",                           "+38.645724,-90.302032",
      "103 YELLOW",                           "+38.648486,-90.305553",
      "105 YELLOW",                           "+38.648018,-90.305074",
      "106 YELLOW",                           "+38.647146,-90.305680",
      "107 YELLOW",                           "+38.647482,-90.305701",
      "111 YELLOW",                           "+38.648651,-90.305520",
      "114 YELLOW",                           "+38.648532,-90.306192",
      "115 YELLOW",                           "+38.648216,-90.306932",
      "134 YELLOW",                           "+38.647743,-90.306494",
      "141 YELLOW",                           "+38.648588,-90.307721",
      "146 YELLOW",                           "+38.648138,-90.306256",
      "149 YELLOW",                           "+38.648818,-90.306363",
      "156 YELLOW",                           "+38.649409,-90.308369",
      "251 YELLOW",                           "+38.646908,-90.305996",
      "257 YELLOW",                           "+38.646830,-90.304728"
  });
}
