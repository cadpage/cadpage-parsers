package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA34Parser;

/**
 * Golden, CO
 */
public class COGoldenParser extends DispatchA34Parser {
  
  public COGoldenParser() {
    super("GOLDEN", "CO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    removeWords("LA");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,CADTOFIRE@CITYOFGOLDEN.NET";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Golden Fire") && !subject.startsWith("CAD call ")) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equals("COORS BREWERY")) city = "GOLDEN";
    return super.adjustMapCity(city);
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    address = address.replace("HWY HWY", "HWY");
    address = MM_TAIL_PTN.matcher(address).replaceFirst("");
    address = I70_PTN.matcher(address).replaceAll("I 70");
    address = TRAIL_HWY_PTN.matcher(address).replaceAll("$1");
    Matcher match  = REVERSE_INTERSECT_PTN.matcher(address);
    if (match.matches()) {
      String part1 = match.group(1);
      String part2 = match.group(2);
      if (!part1.equals("I 70") &&
          (!part1.equals("HWY 6") || part2.equals("I 70"))) {
        address = part2 + " & " + part1;
      }
    }
    return address;
  }
  private static final Pattern MM_TAIL_PTN = Pattern.compile(" +[^ ]*MM$");
  private static final Pattern I70_PTN = Pattern.compile("\\bI-?70\\b");
  private static final Pattern TRAIL_HWY_PTN = Pattern.compile("\\b(I 70|HWY 6|HWY 93|C470) +HWY\\b");
  private static final Pattern REVERSE_INTERSECT_PTN = Pattern.compile("(.*?) *& *(HWY 6|HWY 93|I 70)");
      

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1 CHIMNEY GULCH TRAIL",      "39.747845,-105.232959",
      "1 HWY 93",                   "39.755005,-105.236414",
      "1 W 44TH AVE",             "39.763763,-105.212699",
      
      "260 HWY 6",                  "39.746116,-105.397768",
      "261 HWY 6",                  "39.737476,-105.388593",
      "262 HWY 6",                  "39.736715,-105.372560",
      "263 HWY 6",                  "39.736397,-105.358752",
      "264 HWY 6",                  "39.740600,-105.343095",
      "265 HWY 6",                  "39.740923,-105.324844",
      "266 HWY 6",                  "39.740238,-105.311510",
      "267 HWY 6",                  "39.745050,-105.301929",
      "268 HWY 6",                  "39.741839,-105.285651",
      "269 HWY 6",                  "39.742149,-105.269388",
      "270 HWY 6",                  "39.741363,-105.257450",
      "271 HWY 6",                  "39.751683,-105.247191",
      "HWY 6 & 19TH ST",            "39.745469,-105.223711",
      "HWY 6 & HWY 93",             "39.755005,-105.236414",
      "HWY 6 & TRIPP RD",           "39.737731,-105.217038",
      "HWY 6 & W COLFAX AVE",       "39.725321,-105.189357",
      
      "HWY 93 & WASHINGTON AVE",    "39.767772,-105.235384",

      "I 70 & C470",                "39.712233,-105.194185",
      "I 70 & HWY 6",               "39.725338,-105.179379",
      "I 70 & MORR HWY",            "39.698993,-105.204549",
      "I 70",                       "39.719429,-105.188220",
      

  });
}
