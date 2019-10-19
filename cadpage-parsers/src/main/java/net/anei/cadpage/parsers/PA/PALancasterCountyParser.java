package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lancaster County, PA
 */
public class PALancasterCountyParser extends FieldProgramParser {
  
  public PALancasterCountyParser() {
    super(CITY_LIST, "LANCASTER COUNTY", "PA",
           "CITY ADDR! X/Z+? UNIT TIME%");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "911@lcwc911.us,messaging@iamresponding.com,@everbridge.net,@den.everbridge.net,@den2.everbridge.net,@smtpic-ne.prd1.everbridge.net,141000,89361";
  }
  
  private static final Pattern XML_COMMENT_PTN = Pattern.compile("<!--.*?-->");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = XML_COMMENT_PTN.matcher(body).replaceAll("");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (! body.contains("~")) return false;
    data.strSource = subject;
    
    int pt = body.lastIndexOf('^');
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace(" BOROUGH", " BORO").replace(" TOWNSHIP", " TWP");
    return parseFields(body.split("~"), 3, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+[,A-Z0-9]*");
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private static final Pattern CITY_DELIM = Pattern.compile("\n| / ");
  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*) (DE|MD|PA)");
  private static final Pattern COUNTY_CITY_PTN = Pattern.compile("(?:CHESTER|DAUPHIN|LEBANON) (?!COUNTY)(.*)");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0) {
        Matcher match = CITY_DELIM.matcher(field);
        if (match.find()) {
          data.strCall = field.substring(0,match.start()).trim();
          data.strCity = field.substring(match.end()).trim();
        } else {
          parseAddress(StartType.START_CALL, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
          if (data.strCity.length() == 0) abort();
        }
        match = CITY_ST_PTN.matcher(data.strCity);
        if (match.matches()) {
          data.strCity = match.group(1);
          data.strState = match.group(2);
        }
        match = COUNTY_CITY_PTN.matcher(data.strCity);
        if (match.matches()) data.strCity = match.group(1);
        data.strCity = stripFieldEnd(data.strCity, " BORO");
        if (data.strCity.startsWith("LANC") && !data.strCity.endsWith(" TWP")) data.strCity = "LANCASTER";
      }
      
      if (data.strCall.length() == 0) {
        data.strCall = data.strSource;
        data.strSource = "";
        if (data.strCall.length() == 0) abort();
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CITY ST";
    }
  }
  
  private static Pattern LANC_PTN = Pattern.compile("\\bLANC\\b", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = LANC_PTN.matcher(field).replaceAll("LANCASTER");
      super.parse(field, data);
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
  private static final Pattern PART_TIME_PTN = Pattern.compile("[\\d:]*");
  private class MyTimeField extends TimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (TIME_PTN.matcher(field).matches()) {
        data.strTime = field;
        return true;
      }
      return PART_TIME_PTN.matcher(field).matches();
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return ROUTE_30_PTN.matcher(address).replaceAll("US 30");
  }
  private static final Pattern ROUTE_30_PTN = Pattern.compile("\\b(?:RT|ROUTE) *30\\b");
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1 COLONIAL CREST DR",                  "+40.075698,-76.356599",
      "2 COLONIAL CREST DR",                  "+40.075811,-76.356390",
      "3 COLONIAL CREST DR",                  "+40.075903,-76.356216",
      "4 COLONIAL CREST DR",                  "+40.076085,-76.355555",
      "5 COLONIAL CREST DR",                  "+40.075859,-76.355559",
      "6 COLONIAL CREST DR",                  "+40.075668,-76.355566",
      "7 COLONIAL CREST DR",                  "+40.075158,-76.356013",
      "8 COLONIAL CREST DR",                  "+40.075035,-76.356252",
      "9 COLONIAL CREST DR",                  "+40.074921,-76.356460",
      "28 COLONIAL CREST DR",                 "+40.074667,-76.355483",
      "29 COLONIAL CREST DR",                 "+40.074541,-76.355717",
      "30 COLONIAL CREST DR",                 "+40.074434,-76.355920",
      "32 COLONIAL CREST DR",                 "+40.075663,-76.354762",
      "33 COLONIAL CREST DR",                 "+40.075663,-76.354762",
      "34 COLONIAL CREST DR",                 "+40.075663,-76.354762",
      "35 COLONIAL CREST DR",                 "+40.075663,-76.354762",
      "36 COLONIAL CREST DR",                 "+40.075667,-76.354480",
      "37 COLONIAL CREST DR",                 "+40.075667,-76.354480",
      "38 COLONIAL CREST DR",                 "+40.075667,-76.354480",
      "39 COLONIAL CREST DR",                 "+40.075667,-76.354480",
      "40 COLONIAL CREST DR",                 "+40.075739,-76.354482",
      "41 COLONIAL CREST DR",                 "+40.075739,-76.354482",
      "42 COLONIAL CREST DR",                 "+40.075739,-76.354482",
      "43 COLONIAL CREST DR",                 "+40.075739,-76.354482",
      "44 COLONIAL CREST DR",                 "+40.075739,-76.354770",
      "45 COLONIAL CREST DR",                 "+40.075739,-76.354770",
      "46 COLONIAL CREST DR",                 "+40.075739,-76.354770",
      "47 COLONIAL CREST DR",                 "+40.075739,-76.354770",
      "48 COLONIAL CREST DR",                 "+40.076067,-76.355203",
      "49 COLONIAL CREST DR",                 "+40.076031,-76.355203",
      "50 COLONIAL CREST DR",                 "+40.075982,-76.355217",
      "51 COLONIAL CREST DR",                 "+40.075945,-76.355215",
      "52 COLONIAL CREST DR",                 "+40.075912,-76.355215",
      "53 COLONIAL CREST DR",                 "+40.075860,-76.355215",
      "54 COLONIAL CREST DR",                 "+40.075850,-76.355214",
      "55 COLONIAL CREST DR",                 "+40.075825,-76.355216",
      "56 COLONIAL CREST DR",                 "+40.075778,-76.355201",
      "58 COLONIAL CREST DR",                 "+40.076057,-76.354692",
      "59 COLONIAL CREST DR",                 "+40.076057,-76.354692",
      "60 COLONIAL CREST DR",                 "+40.076057,-76.354692",
      "61 COLONIAL CREST DR",                 "+40.076057,-76.354692",
      "62 COLONIAL CREST DR",                 "+40.076061,-76.354367",
      "63 COLONIAL CREST DR",                 "+40.076061,-76.354367",
      "64 COLONIAL CREST DR",                 "+40.076061,-76.354367",
      "65 COLONIAL CREST DR",                 "+40.076061,-76.354367",
      "66 COLONIAL CREST DR",                 "+40.076133,-76.354374",
      "67 COLONIAL CREST DR",                 "+40.076133,-76.354374",
      "68 COLONIAL CREST DR",                 "+40.076133,-76.354374",
      "69 COLONIAL CREST DR",                 "+40.076133,-76.354374",
      "70 COLONIAL CREST DR",                 "+40.076135,-76.354690",
      "71 COLONIAL CREST DR",                 "+40.076135,-76.354690",
      "72 COLONIAL CREST DR",                 "+40.076135,-76.354690",
      "73 COLONIAL CREST DR",                 "+40.076135,-76.354690",
      "74 COLONIAL CREST DR",                 "+40.076244,-76.353747",
      "75 COLONIAL CREST DR",                 "+40.076244,-76.353747",
      "76 COLONIAL CREST DR",                 "+40.076244,-76.353747",
      "77 COLONIAL CREST DR",                 "+40.076244,-76.353747",
      "78 COLONIAL CREST DR",                 "+40.076176,-76.353744",
      "79 COLONIAL CREST DR",                 "+40.076176,-76.353744",
      "80 COLONIAL CREST DR",                 "+40.076176,-76.353744",
      "81 COLONIAL CREST DR",                 "+40.076176,-76.353744",
      "82 COLONIAL CREST DR",                 "+40.076001,-76.353706",
      "83 COLONIAL CREST DR",                 "+40.076001,-76.353706",
      "84 COLONIAL CREST DR",                 "+40.076001,-76.353706",
      "85 COLONIAL CREST DR",                 "+40.076001,-76.353706",
      "86 COLONIAL CREST DR",                 "+40.075929,-76.353709",
      "87 COLONIAL CREST DR",                 "+40.075929,-76.353709",
      "88 COLONIAL CREST DR",                 "+40.075929,-76.353709",
      "89 COLONIAL CREST DR",                 "+40.075929,-76.353709",
      "90 COLONIAL CREST DR",                 "+40.075754,-76.353743",
      "91 COLONIAL CREST DR",                 "+40.075754,-76.353743",
      "92 COLONIAL CREST DR",                 "+40.075754,-76.353743",
      "93 COLONIAL CREST DR",                 "+40.075754,-76.353743",
      "94 COLONIAL CREST DR",                 "+40.075677,-76.353743",
      "95 COLONIAL CREST DR",                 "+40.075677,-76.353743",
      "96 COLONIAL CREST DR",                 "+40.075677,-76.353743",
      "97 COLONIAL CREST DR",                 "+40.075677,-76.353743",
      "98 COLONIAL CREST DR",                 "+40.075680,-76.353472",
      "99 COLONIAL CREST DR",                 "+40.075680,-76.353472",
      "100 COLONIAL CREST DR",                "+40.075680,-76.353472",
      "101 COLONIAL CREST DR",                "+40.075680,-76.353472",
      "102 COLONIAL CREST DR",                "+40.075751,-76.353474",
      "103 COLONIAL CREST DR",                "+40.075751,-76.353474",
      "104 COLONIAL CREST DR",                "+40.075751,-76.353474",
      "105 COLONIAL CREST DR",                "+40.075751,-76.353474",
      "106 COLONIAL CREST DR",                "+40.075928,-76.353437",
      "107 COLONIAL CREST DR",                "+40.075928,-76.353437",
      "108 COLONIAL CREST DR",                "+40.075928,-76.353437",
      "109 COLONIAL CREST DR",                "+40.075928,-76.353437",
      "110 COLONIAL CREST DR",                "+40.075999,-76.353445",
      "111 COLONIAL CREST DR",                "+40.075999,-76.353445",
      "112 COLONIAL CREST DR",                "+40.075999,-76.353445",
      "113 COLONIAL CREST DR",                "+40.075999,-76.353445",
      "114 COLONIAL CREST DR",                "+40.076176,-76.353476",
      "115 COLONIAL CREST DR",                "+40.076176,-76.353476",
      "116 COLONIAL CREST DR",                "+40.076176,-76.353476",
      "117 COLONIAL CREST DR",                "+40.076176,-76.353476",
      "118 COLONIAL CREST DR",                "+40.076244,-76.353476",
      "119 COLONIAL CREST DR",                "+40.076244,-76.353476",
      "120 COLONIAL CREST DR",                "+40.076244,-76.353476",
      "121 COLONIAL CREST DR",                "+40.076244,-76.353476",
      "122 COLONIAL CREST DR",                "+40.076232,-76.352935",
      "123 COLONIAL CREST DR",                "+40.076207,-76.352935",
      "124 COLONIAL CREST DR",                "+40.076160,-76.352935",
      "125 COLONIAL CREST DR",                "+40.076122,-76.352934",
      "126 COLONIAL CREST DR",                "+40.076068,-76.352919",
      "127 COLONIAL CREST DR",                "+40.076034,-76.352920",
      "128 COLONIAL CREST DR",                "+40.075979,-76.352935",
      "129 COLONIAL CREST DR",                "+40.075943,-76.352935",
      "130 COLONIAL CREST DR",                "+40.075896,-76.352919",
      "131 COLONIAL CREST DR",                "+40.075735,-76.352918",
      "132 COLONIAL CREST DR",                "+40.075696,-76.352920",
      "133 COLONIAL CREST DR",                "+40.075565,-76.352921",
      "134 COLONIAL CREST DR",                "+40.075614,-76.352933",
      "135 COLONIAL CREST DR",                "+40.075563,-76.352934",
      "136 COLONIAL CREST DR",                "+40.075526,-76.352917",
      "137 COLONIAL CREST DR",                "+40.075474,-76.352919",
      "138 COLONIAL CREST DR",                "+40.075426,-76.352905",
      "139 COLONIAL CREST DR",                "+40.075394,-76.352904",
      "140 COLONIAL CREST DR",                "+40.075292,-76.352594",
      "141 COLONIAL CREST DR",                "+40.075330,-76.352595",
      "142 COLONIAL CREST DR",                "+40.075378,-76.352610",
      "143 COLONIAL CREST DR",                "+40.075419,-76.352609",
      "144 COLONIAL CREST DR",                "+40.075464,-76.352609",
      "145 COLONIAL CREST DR",                "+40.075505,-76.352610",
      "146 COLONIAL CREST DR",                "+40.075553,-76.352595",
      "147 COLONIAL CREST DR",                "+40.075593,-76.352595",
      "148 COLONIAL CREST DR",                "+40.075637,-76.352596",
      "149 COLONIAL CREST DR",                "+40.075854,-76.352607",
      "150 COLONIAL CREST DR",                "+40.075894,-76.352607",
      "151 COLONIAL CREST DR",                "+40.075939,-76.352608",
      "152 COLONIAL CREST DR",                "+40.075981,-76.352606",
      "153 COLONIAL CREST DR",                "+40.076029,-76.352623",
      "154 COLONIAL CREST DR",                "+40.076069,-76.352623",
      "155 COLONIAL CREST DR",                "+40.076114,-76.352607",
      "156 COLONIAL CREST DR",                "+40.076154,-76.352607",
      "157 COLONIAL CREST DR",                "+40.076200,-76.352610",
      "158 COLONIAL CREST DR",                "+40.076224,-76.352081",
      "159 COLONIAL CREST DR",                "+40.076224,-76.352081",
      "160 COLONIAL CREST DR",                "+40.076224,-76.352081",
      "161 COLONIAL CREST DR",                "+40.076224,-76.352081",
      "162 COLONIAL CREST DR",                "+40.076146,-76.352075",
      "163 COLONIAL CREST DR",                "+40.076146,-76.352075",
      "164 COLONIAL CREST DR",                "+40.076146,-76.352075",
      "165 COLONIAL CREST DR",                "+40.076146,-76.352075",
      "166 COLONIAL CREST DR",                "+40.075957,-76.352059",
      "167 COLONIAL CREST DR",                "+40.075957,-76.352059",
      "168 COLONIAL CREST DR",                "+40.075957,-76.352059",
      "169 COLONIAL CREST DR",                "+40.075957,-76.352059",
      "170 COLONIAL CREST DR",                "+40.075886,-76.352058",
      "171 COLONIAL CREST DR",                "+40.075886,-76.352058",
      "172 COLONIAL CREST DR",                "+40.075886,-76.352058",
      "173 COLONIAL CREST DR",                "+40.075886,-76.352058",
      "174 COLONIAL CREST DR",                "+40.075683,-76.352035",
      "175 COLONIAL CREST DR",                "+40.075683,-76.352035",
      "176 COLONIAL CREST DR",                "+40.075683,-76.352035",
      "177 COLONIAL CREST DR",                "+40.075683,-76.352035",
      "178 COLONIAL CREST DR",                "+40.075611,-76.352028",
      "179 COLONIAL CREST DR",                "+40.075611,-76.352028",
      "180 COLONIAL CREST DR",                "+40.075611,-76.352028",
      "181 COLONIAL CREST DR",                "+40.075611,-76.352028",
      "182 COLONIAL CREST DR",                "+40.075405,-76.352013",
      "183 COLONIAL CREST DR",                "+40.075405,-76.352013",
      "184 COLONIAL CREST DR",                "+40.075405,-76.352013",
      "185 COLONIAL CREST DR",                "+40.075405,-76.352013",
      "186 COLONIAL CREST DR",                "+40.075330,-76.352019",
      "187 COLONIAL CREST DR",                "+40.075330,-76.352019",
      "188 COLONIAL CREST DR",                "+40.075330,-76.352019",
      "189 COLONIAL CREST DR",                "+40.075330,-76.352019",
      "192 COLONIAL CREST DR",                "+40.075324,-76.351728",
      "193 COLONIAL CREST DR",                "+40.075324,-76.351728",
      "194 COLONIAL CREST DR",                "+40.075324,-76.351728",
      "195 COLONIAL CREST DR",                "+40.075324,-76.351728",
      "196 COLONIAL CREST DR",                "+40.075405,-76.351734",
      "197 COLONIAL CREST DR",                "+40.075405,-76.351734",
      "198 COLONIAL CREST DR",                "+40.075405,-76.351734",
      "199 COLONIAL CREST DR",                "+40.075405,-76.351734",
      "200 COLONIAL CREST DR",                "+40.075607,-76.351745",
      "201 COLONIAL CREST DR",                "+40.075607,-76.351745",
      "202 COLONIAL CREST DR",                "+40.075607,-76.351745",
      "203 COLONIAL CREST DR",                "+40.075607,-76.351745",
      "204 COLONIAL CREST DR",                "+40.075686,-76.351746",
      "205 COLONIAL CREST DR",                "+40.075686,-76.351746",
      "206 COLONIAL CREST DR",                "+40.075686,-76.351746",
      "207 COLONIAL CREST DR",                "+40.075686,-76.351746",
      "208 COLONIAL CREST DR",                "+40.075885,-76.351763",
      "209 COLONIAL CREST DR",                "+40.075885,-76.351763",
      "210 COLONIAL CREST DR",                "+40.075885,-76.351763",
      "211 COLONIAL CREST DR",                "+40.075885,-76.351763",
      "212 COLONIAL CREST DR",                "+40.075962,-76.351768",
      "213 COLONIAL CREST DR",                "+40.075962,-76.351768",
      "214 COLONIAL CREST DR",                "+40.075962,-76.351768",
      "215 COLONIAL CREST DR",                "+40.075962,-76.351768",
      "216 COLONIAL CREST DR",                "+40.076153,-76.351817",
      "217 COLONIAL CREST DR",                "+40.076153,-76.351817",
      "218 COLONIAL CREST DR",                "+40.076153,-76.351817",
      "219 COLONIAL CREST DR",                "+40.076153,-76.351817",
      "220 COLONIAL CREST DR",                "+40.076228,-76.351822",
      "221 COLONIAL CREST DR",                "+40.076228,-76.351822",
      "222 COLONIAL CREST DR",                "+40.076228,-76.351822",
      "223 COLONIAL CREST DR",                "+40.076228,-76.351822",
      "224 COLONIAL CREST DR",                "+40.075093,-76.355205",
      "225 COLONIAL CREST DR",                "+40.075093,-76.355205",
      "226 COLONIAL CREST DR",                "+40.075033,-76.355148",
      "227 COLONIAL CREST DR",                "+40.075033,-76.355148",
      "228 COLONIAL CREST DR",                "+40.074924,-76.355049",
      "229 COLONIAL CREST DR",                "+40.074924,-76.355049",
      "230 COLONIAL CREST DR",                "+40.074836,-76.354970",
      "231 COLONIAL CREST DR",                "+40.074836,-76.354970",
      "232 COLONIAL CREST DR",                "+40.074597,-76.354752",
      "233 COLONIAL CREST DR",                "+40.074597,-76.354752",
      "234 COLONIAL CREST DR",                "+40.074531,-76.354694",
      "235 COLONIAL CREST DR",                "+40.074531,-76.354694",
      "236 COLONIAL CREST DR",                "+40.074435,-76.354607",
      "237 COLONIAL CREST DR",                "+40.074435,-76.354607",
      "238 COLONIAL CREST DR",                "+40.074372,-76.354547",
      "239 COLONIAL CREST DR",                "+40.074372,-76.354547",
      "240 COLONIAL CREST DR",                "+40.074452,-76.355193",
      "241 COLONIAL CREST DR",                "+40.074407,-76.355151",
      "242 COLONIAL CREST DR",                "+40.074380,-76.355128",
      "243 COLONIAL CREST DR",                "+40.074339,-76.355090",
      "244 COLONIAL CREST DR",                "+40.074309,-76.355046",
      "245 COLONIAL CREST DR",                "+40.074275,-76.355013",
      "246 COLONIAL CREST DR",                "+40.074238,-76.354980",
      "247 COLONIAL CREST DR",                "+40.074199,-76.354946",
      "264 COLONIAL CREST DR",                "+40.074396,-76.354457",
      "265 COLONIAL CREST DR",                "+40.074396,-76.354457",
      "266 COLONIAL CREST DR",                "+40.074471,-76.354524",
      "267 COLONIAL CREST DR",                "+40.074471,-76.354524",
      "268 COLONIAL CREST DR",                "+40.074568,-76.354615",
      "269 COLONIAL CREST DR",                "+40.074568,-76.354615",
      "270 COLONIAL CREST DR",                "+40.074639,-76.354685",
      "271 COLONIAL CREST DR",                "+40.074639,-76.354685",
      "348 COLONIAL CREST DR",                "+40.074370,-76.352414",
      "349 COLONIAL CREST DR",                "+40.074370,-76.352414",
      "350 COLONIAL CREST DR",                "+40.074459,-76.352471",
      "351 COLONIAL CREST DR",                "+40.074459,-76.352471",
      "352 COLONIAL CREST DR",                "+40.074583,-76.352549",
      "353 COLONIAL CREST DR",                "+40.074583,-76.352549",
      "354 COLONIAL CREST DR",                "+40.074652,-76.352592",
      "355 COLONIAL CREST DR",                "+40.074652,-76.352592",
      "356 COLONIAL CREST DR",                "+40.074339,-76.352495",
      "357 COLONIAL CREST DR",                "+40.074339,-76.352495",
      "358 COLONIAL CREST DR",                "+40.074423,-76.352548",
      "359 COLONIAL CREST DR",                "+40.074423,-76.352548",
      "360 COLONIAL CREST DR",                "+40.074549,-76.352622",
      "361 COLONIAL CREST DR",                "+40.074549,-76.352622",
      "362 COLONIAL CREST DR",                "+40.074619,-76.352667",
      "363 COLONIAL CREST DR",                "+40.074619,-76.352667",
      "364 COLONIAL CREST DR",                "+40.074365,-76.352931",
      "365 COLONIAL CREST DR",                "+40.074365,-76.352931",
      "366 COLONIAL CREST DR",                "+40.074468,-76.352945",
      "367 COLONIAL CREST DR",                "+40.074468,-76.352945",
      "368 COLONIAL CREST DR",                "+40.074603,-76.352960",
      "369 COLONIAL CREST DR",                "+40.074603,-76.352960",
      "370 COLONIAL CREST DR",                "+40.074704,-76.352971",
      "371 COLONIAL CREST DR",                "+40.074704,-76.352971",
      "372 COLONIAL CREST DR",                "+40.074361,-76.353028",
      "373 COLONIAL CREST DR",                "+40.074361,-76.353028",
      "374 COLONIAL CREST DR",                "+40.074458,-76.353038",
      "375 COLONIAL CREST DR",                "+40.074458,-76.353038",
      "376 COLONIAL CREST DR",                "+40.074607,-76.353048",
      "377 COLONIAL CREST DR",                "+40.074607,-76.353048",
      "378 COLONIAL CREST DR",                "+40.074701,-76.353060",
      "379 COLONIAL CREST DR",                "+40.074701,-76.353060",
      "380 COLONIAL CREST DR",                "+40.075036,-76.353499",
      "381 COLONIAL CREST DR",                "+40.075036,-76.353499",
      "382 COLONIAL CREST DR",                "+40.075043,-76.353632",
      "383 COLONIAL CREST DR",                "+40.075043,-76.353632",
      "384 COLONIAL CREST DR",                "+40.074969,-76.353508",
      "385 COLONIAL CREST DR",                "+40.074969,-76.353508",
      "386 COLONIAL CREST DR",                "+40.074970,-76.353626",
      "387 COLONIAL CREST DR",                "+40.074970,-76.353626",
      "388 COLONIAL CREST DR",                "+40.075130,-76.355127",
      "389 COLONIAL CREST DR",                "+40.075130,-76.355127",
      "390 COLONIAL CREST DR",                "+40.075060,-76.355057",
      "391 COLONIAL CREST DR",                "+40.075060,-76.355057",
      "392 COLONIAL CREST DR",                "+40.074960,-76.354972",
      "393 COLONIAL CREST DR",                "+40.074960,-76.354972",
      "394 COLONIAL CREST DR",                "+40.074867,-76.354887",
      "395 COLONIAL CREST DR",                "+40.074867,-76.354887",
      "396 COLONIAL CREST DR",                "+40.075080,-76.354596",
      "397 COLONIAL CREST DR",                "+40.075080,-76.354596",
      "398 COLONIAL CREST DR",                "+40.075005,-76.354567",
      "399 COLONIAL CREST DR",                "+40.075005,-76.354567",
      "400 COLONIAL CREST DR",                "+40.074891,-76.354529",
      "401 COLONIAL CREST DR",                "+40.074891,-76.354529",
      "402 COLONIAL CREST DR",                "+40.074810,-76.354500",
      "403 COLONIAL CREST DR",                "+40.074810,-76.354500",
      "404 COLONIAL CREST DR",                "+40.075095,-76.354508",
      "405 COLONIAL CREST DR",                "+40.075095,-76.354508",
      "406 COLONIAL CREST DR",                "+40.075017,-76.354480",
      "407 COLONIAL CREST DR",                "+40.075017,-76.354480",
      "408 COLONIAL CREST DR",                "+40.074908,-76.354440",
      "409 COLONIAL CREST DR",                "+40.074908,-76.354440",
      "410 COLONIAL CREST DR",                "+40.074832,-76.354414",
      "411 COLONIAL CREST DR",                "+40.074832,-76.354414",
      "412 COLONIAL CREST DR",                "+40.075060,-76.353970",
      "413 COLONIAL CREST DR",                "+40.075060,-76.353970",
      "414 COLONIAL CREST DR",                "+40.075055,-76.353837",
      "415 COLONIAL CREST DR",                "+40.075055,-76.353837",
      "416 COLONIAL CREST DR",                "+40.074988,-76.353968",
      "417 COLONIAL CREST DR",                "+40.074988,-76.353968",
      "418 COLONIAL CREST DR",                "+40.074982,-76.353835",
      "419 COLONIAL CREST DR",                "+40.074982,-76.353835",
      "1117 LONG LN",                         "+40.075855,-76.524121",
      "1 TENBY WAY",                          "+40.075848,-76.358203",
      "2 TENBY WAY",                          "+40.075336,-76.358184",
      "3 TENBY WAY",                          "+40.075848,-76.358203",
      "4 TENBY WAY",                          "+40.075336,-76.358184",
      "5 TENBY WAY",                          "+40.075985,-76.358202",
      "6 TENBY WAY",                          "+40.075333,-76.358019",
      "7 TENBY WAY",                          "+40.075985,-76.358202",
      "8 TENBY WAY",                          "+40.075333,-76.358019",
      "9 TENBY WAY",                          "+40.075954,-76.357818",
      "10 TENBY WAY",                         "+40.075334,-76.357823",
      "11 TENBY WAY",                         "+40.075954,-76.357818",
      "12 TENBY WAY",                         "+40.075334,-76.357823",
      "13 TENBY WAY",                         "+40.075952,-76.357634",
      "14 TENBY WAY",                         "+40.075335,-76.357649",
      "15 TENBY WAY",                         "+40.075952,-76.357634",
      "16 TENBY WAY",                         "+40.075335,-76.357649",
      "17 TENBY WAY",                         "+40.076343,-76.357602",
      "18 TENBY WAY",                         "+40.074939,-76.357610",
      "19 TENBY WAY",                         "+40.076343,-76.357602",
      "20 TENBY WAY",                         "+40.074939,-76.357610",
      "21 TENBY WAY",                         "+40.076346,-76.357811",
      "22 TENBY WAY",                         "+40.074943,-76.357795",
      "23 TENBY WAY",                         "+40.076346,-76.357811",
      "24 TENBY WAY",                         "+40.074943,-76.357795",
      "25 TENBY WAY",                         "+40.076347,-76.358001",
      "26 TENBY WAY",                         "+40.074946,-76.358129",
      "27 TENBY WAY",                         "+40.076347,-76.358001",
      "28 TENBY WAY",                         "+40.074946,-76.358129",
      "29 TENBY WAY",                         "+40.076345,-76.358180",
      "30 TENBY WAY",                         "+40.074942,-76.358292",
      "31 TENBY WAY",                         "+40.076345,-76.358180",
      "32 TENBY WAY",                         "+40.074942,-76.358292",
      "33 TENBY WAY",                         "+40.076351,-76.358494",
      "34 TENBY WAY",                         "+40.075349,-76.358750",
      "35 TENBY WAY",                         "+40.076351,-76.358494",
      "36 TENBY WAY",                         "+40.075349,-76.358750",
      "37 TENBY WAY",                         "+40.076351,-76.358679",
      "38 TENBY WAY",                         "+40.075399,-76.358928",
      "39 TENBY WAY",                         "+40.076351,-76.358679",
      "40 TENBY WAY",                         "+40.075399,-76.358928",
      "41 TENBY WAY",                         "+40.076107,-76.358773",
      "43 TENBY WAY",                         "+40.076107,-76.358773",
      "45 TENBY WAY",                         "+40.075965,-76.358770",
      "47 TENBY WAY",                         "+40.075965,-76.358770",
      "49 TENBY WAY",                         "+40.075830,-76.358775",
      "51 TENBY WAY",                         "+40.075830,-76.358775",
      "1 WELSH DR",                           "+40.073493,-76.357577",
      "2 WELSH DR",                           "+40.072858,-76.358452",
      "3 WELSH DR",                           "+40.073493,-76.357577",
      "4 WELSH DR",                           "+40.072858,-76.358452",
      "5 WELSH DR",                           "+40.073475,-76.357761",
      "6 WELSH DR",                           "+40.072752,-76.358569",
      "7 WELSH DR",                           "+40.073475,-76.357761",
      "8 WELSH DR",                           "+40.072752,-76.358569",
      "9 WELSH DR",                           "+40.073456,-76.357938",
      "10 WELSH DR",                          "+40.072640,-76.358696",
      "11 WELSH DR",                          "+40.073456,-76.357938",
      "12 WELSH DR",                          "+40.072640,-76.358696",
      "13 WELSH DR",                          "+40.073421,-76.358298",
      "14 WELSH DR",                          "+40.072529,-76.358818",
      "15 WELSH DR",                          "+40.073421,-76.358298",
      "16 WELSH DR",                          "+40.072529,-76.358818",
      "17 WELSH DR",                          "+40.073363,-76.358450",
      "18 WELSH DR",                          "+40.072587,-76.359648",
      "19 WELSH DR",                          "+40.073363,-76.358450",
      "20 WELSH DR",                          "+40.072587,-76.359648",
      "21 WELSH DR",                          "+40.073897,-76.357779",
      "22 WELSH DR",                          "+40.072732,-76.359636",
      "23 WELSH DR",                          "+40.073897,-76.357779",
      "24 WELSH DR",                          "+40.072732,-76.359636",
      "25 WELSH DR",                          "+40.073779,-76.357883",
      "26 WELSH DR",                          "+40.072885,-76.359626",
      "27 WELSH DR",                          "+40.073779,-76.357883",
      "28 WELSH DR",                          "+40.072885,-76.359626",
      "29 WELSH DR",                          "+40.073691,-76.358171",
      "30 WELSH DR",                          "+40.073121,-76.359455",
      "31 WELSH DR",                          "+40.073691,-76.358171",
      "32 WELSH DR",                          "+40.073121,-76.359455",
      "33 WELSH DR",                          "+40.073690,-76.358352",
      "34 WELSH DR",                          "+40.073181,-76.359278",
      "35 WELSH DR",                          "+40.073690,-76.358352",
      "36 WELSH DR",                          "+40.073181,-76.359278",
      "37 WELSH DR",                          "+40.073672,-76.358550",
      "38 WELSH DR",                          "+40.073240,-76.359113",
      "39 WELSH DR",                          "+40.073672,-76.358550",
      "40 WELSH DR",                          "+40.073240,-76.359113",
      "41 WELSH DR",                          "+40.073665,-76.358715",
      "42 WELSH DR",                          "+40.073616,-76.359310",
      "43 WELSH DR",                          "+40.073665,-76.358715",
      "44 WELSH DR",                          "+40.073616,-76.359310",
      "46 WELSH DR",                          "+40.073544,-76.359475",
      "48 WELSH DR",                          "+40.073544,-76.359475",
      "50 WELSH DR",                          "+40.073422,-76.359768",
      "52 WELSH DR",                          "+40.073422,-76.359768",
      "54 WELSH DR",                          "+40.073519,-76.359921",
      "56 WELSH DR",                          "+40.073519,-76.359921",
      "58 WELSH DR",                          "+40.073819,-76.360067",
      "60 WELSH DR",                          "+40.073819,-76.360067",
      "62 WELSH DR",                          "+40.073830,-76.359887",
      "64 WELSH DR",                          "+40.073830,-76.359887",
      "101 WELSH DR",                         "+40.074234,-76.358837",
      "102 WELSH DR",                         "+40.074206,-76.359460",
      "103 WELSH DR",                         "+40.074234,-76.358837",
      "104 WELSH DR",                         "+40.074206,-76.359460",
      "105 WELSH DR",                         "+40.074239,-76.358658",
      "106 WELSH DR",                         "+40.074197,-76.359641",
      "107 WELSH DR",                         "+40.074239,-76.358658",
      "108 WELSH DR",                         "+40.074197,-76.359641",
      "109 WELSH DR",                         "+40.074252,-76.358475",
      "110 WELSH DR",                         "+40.074193,-76.359824",
      "111 WELSH DR",                         "+40.074252,-76.358475",
      "112 WELSH DR",                         "+40.074193,-76.359824",
      "113 WELSH DR",                         "+40.074260,-76.358302",
      "114 WELSH DR",                         "+40.074185,-76.359999",
      "115 WELSH DR",                         "+40.074260,-76.358302",
      "116 WELSH DR",                         "+40.074185,-76.359999",
      "117 WELSH DR",                         "+40.074268,-76.358123",
      "118 WELSH DR",                         "+40.074231,-76.360556",
      "119 WELSH DR",                         "+40.074268,-76.358123",
      "120 WELSH DR",                         "+40.074231,-76.360556",
      "121 WELSH DR",                         "+40.074286,-76.357664",
      "122 WELSH DR",                         "+40.074366,-76.360566",
      "123 WELSH DR",                         "+40.074286,-76.357664",
      "124 WELSH DR",                         "+40.074366,-76.360566",
      "125 WELSH DR",                         "+40.074437,-76.357664",
      "126 WELSH DR",                         "+40.074507,-76.360579",
      "127 WELSH DR",                         "+40.074437,-76.357664",
      "128 WELSH DR",                         "+40.074507,-76.360579",
      "129 WELSH DR",                         "+40.074558,-76.357661",
      "130 WELSH DR",                         "+40.074812,-76.360115",
      "131 WELSH DR",                         "+40.074558,-76.357661",
      "132 WELSH DR",                         "+40.074812,-76.360115",
      "133 WELSH DR",                         "+40.074645,-76.358417",
      "134 WELSH DR",                         "+40.074815,-76.359938",
      "135 WELSH DR",                         "+40.074645,-76.358417",
      "136 WELSH DR",                         "+40.074815,-76.359938",
      "137 WELSH DR",                         "+40.074638,-76.358601",
      "138 WELSH DR",                         "+40.074825,-76.359755",
      "139 WELSH DR",                         "+40.074638,-76.358601",
      "140 WELSH DR",                         "+40.074825,-76.359755",
      "141 WELSH DR",                         "+40.074931,-76.358804",
      "143 WELSH DR",                         "+40.074931,-76.358804",
      "145 WELSH DR",                         "+40.075048,-76.358901",
      "147 WELSH DR",                         "+40.075048,-76.358901"
  });
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "LANC",
    "LANC CITY",
    "LANCASTER",
    "LANCASTER CITY",
    
    // Boroughs
    "ADAMSTOWN BORO",
    "AKRON BORO",
    "CHRISTIANA BORO",
    "COLUMBIA BORO",
    "DENVER BORO",
    "EAST PETERSBURG BORO",
    "ELIZABETHTOWN BORO",
    "EPHRATA BORO",
    "LITITZ BORO",
    "MANHEIM BORO",
    "MARIETTA BORO",
    "MILLERSVILLE BORO",
    "MOUNT JOY BORO",
    "MOUNTVILLE BORO",
    "NEW HOLLAND BORO",
    "QUARRYVILLE BORO",
    "STRASBURG BORO",
    "TERRE HILL BORO",
    
    // Townships
    "BART TWP",
    "BRECKNOCK TWP",
    "CAERNARVON TWP",
    "CLAY TWP",
    "COLERAIN TWP",
    "CONESTOGA TWP",
    "CONOY TWP",
    "DRUMORE TWP",
    "EARL TWP",
    "EAST COCALICO TWP",
    "EAST DONEGAL TWP",
    "EAST DRUMORE TWP",
    "EAST EARL TWP",
    "EAST HEMPFIELD TWP",
    "EAST LAMPETER TWP",
    "EDEN TWP",
    "ELIZABETH TWP",
    "EPHRATA TWP",
    "FULTON TWP",
    "LANCASTER TWP",
    "LEACOCK TWP",
    "LITTLE BRITAIN TWP",
    "MANHEIM TWP",
    "MANOR TWP",
    "MARTIC TWP",
    "MOUNT JOY TWP",
    "PARADISE TWP",
    "PENN TWP",
    "PEQUEA TWP",
    "PROVIDENCE TWP",
    "RAPHO TWP",
    "SADSBURY TWP",
    "SALISBURY TWP",
    "STRASBURG TWP",
    "UPPER LEACOCK TWP",
    "WARWICK TWP",
    "WEST COCALICO TWP",
    "WEST DONEGAL TWP",
    "WEST EARL TWP",
    "WEST HEMPFIELD TWP",
    "WEST LAMPETER TWP",
    
    // Census-designated places
    "BAINBRIDGE",
    "BIRD-IN-HAND",
    "BLUE BALL",
    "BOWMANSVILLE",
    "BRICKERVILLE",
    "BROWNSTOWN",
    "CHURCHTOWN",
    "CLAY",
    "CONESTOGA",
    "EAST EARL",
    "FALMOUTH",
    "FARMERSVILLE",
    "FIVEPOINTVILLE",
    "GAP",
    "GEORGETOWN",
    "GOODVILLE",
    "GORDONVILLE",
    "HOPELAND",
    "INTERCOURSE",
    "KIRKWOOD",
    "LAMPETER",
    "LANDISVILLE",
    "LEOLA",
    "LITTLE BRITAIN",
    "MAYTOWN",
    "MORGANTOWN",
    "PARADISE",
    "PENRYN",
    "REAMSTOWN",
    "REFTON",
    "REINHOLDS",
    "RHEEMS",
    "RONKS",
    "ROTHSVILLE",
    "SALUNGA",
    "SCHOENECK",
    "SMOKETOWN",
    "SOUDERSBURG",
    "STEVENS",
    "SWARTZVILLE",
    "WAKEFIELD",
    "WASHINGTON BORO",
    "WILLOW STREET",
    "WITMER",
 
    // Other communities
    "BAUSMAN",
    "BROWNSTOWN",
    "BLAINSPORT",
    "BUCK",
    "COCALICO",
    "CONEWAGO",
    "CRESWELL",
    "DILLERVILLE",
    "ELM",
    "FERTILITY",
    "HEMPFIELD",
    "HINKLETOWN",
    "HOLTWOOD",
    "KINZERS",
    "KISSEL HILL",
    "LEAMAN PLACE",
    "LYNDON",
    "MARTINDALE",
    "MASTERSONVILLE",
    "MECHANICS GROVE",
    "NARVON",
    "NEW DANVILLE",
    "NEFFSVILLE",
    "NICKEL MINES",
    "PEQUEA",
    "SAFE HARBOR",
    "SILVER SPRING",
    "TALMAGE",
    "WHITE HORSE",
    
    // Other counties
    "BERKS COUNTY",
    "BUCKS COUNTY",
    "CECIL COUNTY MD",
    "CHESTER COUNTY",
    "CUMBERLAND COUNTY",
    "DAUPHIN COUNTY",
    "LEBANON COUNTY",
    "NEW CASTLE COUNTY DE",
    "YORK COUNTY",
    
    "CHESTER ATGLEN BORO",
    "LEBANON SOUTH LONDONDERRY TWP",
    "DAUPHIN CONEWAGO TWP",
    "DAUPHIN LONDONDERRY TWP"

  };
}
