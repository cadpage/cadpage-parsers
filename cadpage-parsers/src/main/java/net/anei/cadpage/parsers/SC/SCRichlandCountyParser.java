package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class SCRichlandCountyParser extends FieldProgramParser {

  public SCRichlandCountyParser() {
    super("RICHLAND COUNTY", "SC",
          "Address:ADDR! Problem:CALL! Facility:PLACE MapGrid:MAP");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "2002000004,@alert.active911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("911 Page") && body.startsWith("Comment:")) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body.substring(9).trim();
      return true;
    }

    // If the regular message parser handles this all is well
    body = body.replace(" MapGrid ", " MapGrid:");
    if (!super.parseMsg(body, data)) {

      // If not, see if we can get this through a general type parser
      // Which will only accept it caller has identified this as a dispatch page
      if (!isPositiveId()) return false;

      parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
      if (data.strAddress.length() == 0) return false;
      data.strSupp = getLeft();
    }

    // Fix mistyped address
    data.strAddress = data.strAddress.replace("Hardscrabble", "Hard Scrabble");
    data.strAddress = data.strAddress.replace("HARDSCRABBLE", "HARD SCRABBLE");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*) ([A-Z]+\\d+(?:,[A-Z0-9,]+)?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_UNIT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strUnit = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }
  }

  private static final Pattern I26_PTN = Pattern.compile("I ?26 MM (\\d++)(?: [EW])?");
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    Matcher match = I26_PTN.matcher(address);
    if (match.matches()) address = "I26 MM " + match.group(1);
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "I20 MM 101 E",                         "+34.216300,-80.537450",
      "I20 MM 102 E",                         "+34.215430,-80.520040",
      "I20 MM 104 W",                         "+34.210600,-80.485850",
      "I20 MM 106 W",                         "+34.208240,-80.451060",
      "I20 MM 40 E",                          "+33.828110,-81.435930",
      "I20 MM 42 E",                          "+33.847220,-81.409900",
      "I20 MM 45 E",                          "+33.880360,-81.376500",
      "I20 MM 46 E",                          "+33.888900,-81.362530",
      "I20 MM 47 E",                          "+33.897300,-81.348540",
      "I20 MM 47 W",                          "+33.897810,-81.348910",
      "I20 MM 48 E",                          "+33.905980,-81.334560",
      "I20 MM 51 E",                          "+33.927330,-81.289180",
      "I20 MM 54 E",                          "+33.950900,-81.245640",
      "I20 MM 55 E",                          "+33.958230,-81.230830",
      "I20 MM 55 W",                          "+33.958560,-81.230970",
      "I20 MM 57 E",                          "+33.969750,-81.198860",
      "I20 MM 57 W",                          "+33.971080,-81.199090",
      "I20 MM 58 W",                          "+33.983460,-81.192930",
      "I20 MM 59 E",                          "+33.993080,-81.180900",
      "I20 MM 59 W",                          "+33.993310,-81.180990",
      "I20 MM 60 E",                          "+34.003150,-81.168730",
      "I20 MM 60 W",                          "+34.003310,-81.168660",
      "I20 MM 61 E",                          "+34.009320,-81.053300",
      "I20 MM 61 W",                          "+34.009610,-81.153440",
      "I20 MM 62 E",                          "+34.016020,-81.139580",
      "I20 MM 62 W",                          "+34.016190,-81.139800",
      "I20 MM 63 E",                          "+34.027430,-81.126130",
      "I20 MM 63 W",                          "+34.027550,-81.126420",
      "I20 MM 64 W",                          "+34.036070,-81.113800",
      "I20 MM 65 E",                          "+34.038570,-81.096140",
      "I20 MM 65 W",                          "+34.038870,-81.096180",
      "I20 MM 66 E",                          "+34.045050,-81.079960",
      "I20 MM 66 W",                          "+34.045270,-81.080070",
      "I20 MM 67 E",                          "+34.051600,-81.064420",
      "I20 MM 67 W",                          "+34.051740,-81.064590",
      "I20 MM 68 E",                          "+34.061440,-81.047910",
      "I20 MM 69 W",                          "+34.066510,-81.035770",
      "I20 MM 70 E",                          "+34.067820,-81.018360",
      "I20 MM 70 W",                          "+34.068000,-81.018750",
      "I20 MM 71 W",                          "+34.074070,-81.003050",
      "I20 MM 72 E",                          "+34.073110,-80.985630",
      "I20 MM 72 W",                          "+34.073360,-80.985710",
      "I20 MM 73 E",                          "+34.073510,-80.968190",
      "I20 MM 73 W",                          "+34.073850,-80.968380",
      "I20 MM 75 W",                          "+34.070770,-80.934170",
      "I20 MM 76 E",                          "+34.068630,-80.917390",
      "I20 MM 77 E",                          "+34.075520,-80.901950",
      "I20 MM 80 E",                          "+34.095070,-80.854630",
      "I20 MM 80 W",                          "+34.095280,-80.855710",
      "I20 MM 82 E",                          "+34.106460,-80.823580",
      "I20 MM 82 W",                          "+34.106780,-80.823830",
      "I20 MM 84 E",                          "+34.119800,-80.792790",
      "I20 MM 84 W",                          "+34.120090,-80.793020",
      "I20 MM 85 E",                          "+34.127400,-80.778050",
      "I20 MM 85 W",                          "+34.127610,-80.778230",
      "I20 MM 86 E",                          "+34.134880,-80.763070",
      "I20 MM 86 W",                          "+34.135280,-80.763340",
      "I20 MM 87 E",                          "+34.143530,-80.749080",
      "I20 MM 87 W",                          "+34.143820,-80.749400",
      "I20 MM 88 E",                          "+34.152770,-80.735650",
      "I20 MM 88 W",                          "+34.153350,-80.736120",
      "I20 MM 89 E",                          "+34.162120,-80.722280",
      "I20 MM 89 W",                          "+34.162650,-80.722820",
      "I20 MM 90 W",                          "+34.172220,-80.709760",
      "I20 MM 91 E",                          "+34.182800,-80.697850",
      "I20 MM 91 W",                          "+34.183040,-80.698210",
      "I20 MM 92 E",                          "+34.190430,-80.683390",
      "I20 MM 92 W",                          "+34.190840,-80.683620",
      "I20 MM 93 E",                          "+34.197710,-80.668430",
      "I20 MM 93 W",                          "+34.197290,-80.668080",
      "I20 MM 94 E",                          "+34.205080,-80.653330",
      "I20 MM 94 W",                          "+34.205370,-80.653560",
      "I20 MM 95 E",                          "+34.214550,-80.640110",
      "I20 MM 95 W",                          "+34.214710,-80.640130",
      "I20 MM 96 E",                          "+34.218660,-80.623500",
      "I20 MM 96 W",                          "+34.218990,-80.623640",
      "I20 MM 97 E",                          "+34.218260,-80.606640",
      "I20 MM 97 W",                          "+34.218670,-80.606810",
      "I20 MM 98 E",                          "+34.216240,-80.589030",
      "I20 MM 98 W",                          "+34.216740,-80.589100",
      "I20 MM 99 E",                          "+34.214340,-80.571720",
      "I20 MM 99 W",                          "+34.214840,-80.571610",
      "I26 MM 100",                           "+34.116077,-81.192335",
      "I26 MM 100 E",                         "+34.115830,-81.192500",
      "I26 MM 100 W",                         "+34.116220,-81.192340",
      "I26 MM 101",                           "+34.104440,-81.180370",
      "I26 MM 101 E",                         "+34.104200,-81.180560",
      "I26 MM 101 W",                         "+34.104570,-81.180410",
      "I26 MM 102",                           "+34.093455,-81.168544",
      "I26 MM 102 E",                         "+34.093280,-81.168640",
      "I26 MM 102 W",                         "+34.093540,-81.168420",
      "I26 MM 103",                           "+34.082634,-81.157314",
      "I26 MM 103 E",                         "+34.082220,-81.157240",
      "I26 MM 103 W",                         "+34.082390,-81.157000",
      "I26 MM 104",                           "+34.071359,-81.146058",
      "I26 MM 105",                           "+34.060407,-81.135084",
      "I26 MM 105 W",                         "+34.060420,-81.134680",
      "I26 MM 106",                           "+34.049482,-81.123124",
      "I26 MM 106 W",                         "+34.049730,-81.122970",
      "I26 MM 107",                           "+34.038075,-81.111919",
      "I26 MM 107 W",                         "+34.038240,-81.111690",
      "I26 MM 111 E",                         "+33.985760,-81.107360",
      "I26 MM 111 W",                         "+33.985790,-81.106900",
      "I26 MM 112 W",                         "+33.969360,-81.100390",
      "I26 MM 113 E",                         "+33.961270,-81.095610",
      "I26 MM 113 W",                         "+33.961380,-81.095180",
      "I26 MM 114 E",                         "+33.945330,-81.089900",
      "I26 MM 114 W",                         "+33.945370,-81.089590",
      "I26 MM 115 E",                         "+33.934040,-81.079240",
      "I26 MM 115 W",                         "+33.934360,-81.079130",
      "I26 MM 116 E",                         "+33.923330,-81.068570",
      "I26 MM 116 W",                         "+33.923410,-81.068980",
      "I26 MM 117 E",                         "+33.912500,-81.057730",
      "I26 MM 117 W",                         "+33.912740,-81.057530",
      "I26 MM 118 E",                         "+33.900710,-81.048430",
      "I26 MM 118 W",                         "+33.899370,-81.047740",
      "I26 MM 119 E",                         "+33.887870,-81.039480",
      "I26 MM 123 E",                         "+33.832230,-81.020820",
      "I26 MM 128 E",                         "+33.770810,-80.975500",
      "I26 MM 131 W",                         "+33.742280,-80.938690",
      "I26 MM 132 E",                         "+33.729200,-80.931310",
      "I26 MM 134 E",                         "+33.702950,-80.916690",
      "I26 MM 136 E",                         "+33.677230,-80.900790",
      "I26 MM 69 E",                          "+34.066210,-81.035670",
      "I26 MM 74 W",                          "+34.309470,-81.571160",
      "I26 MM 77 E",                          "+34.284640,-81.532030",
      "I26 MM 90 W",                          "+34.187580,-81.340000",
      "I26 MM 91",                            "+34.179152,-81.325758",
      "I26 MM 91 E",                          "+34.179090,-81.326090",
      "I26 MM 91 W",                          "+34.179370,-81.325960",
      "I26 MM 92",                            "+34.172983,-81.310029",
      "I26 MM 93",                            "+34.167533,-81.294065",
      "I26 MM 93 E",                          "+34.167330,-81.294040",
      "I26 MM 93 W",                          "+34.167610,-81.293970",
      "I26 MM 94",                            "+34.161608,-81.278161",
      "I26 MM 95",                            "+34.155612,-81.262177",
      "I26 MM 95 E",                          "+34.155440,-81.262170",
      "I26 MM 95 W",                          "+34.155630,-81.262090",
      "I26 MM 96",                            "+34.149835,-81.246734",
      "I26 MM 97",                            "+34.142816,-81.230906",
      "I26 MM 97 E",                          "+34.142600,-81.230930",
      "I26 MM 97 W",                          "+34.142860,-81.230880",
      "I26 MM 98",                            "+34.135915,-81.215648",
      "I26 MM 98 W",                          "+34.136010,-81.214560",
      "I26 MM 99",                            "+34.126521,-81.203159",
      "I77 MM 1 N",                           "+33.928630,-81.057620",
      "I77 MM 1 S",                           "+33.929990,-81.057870",
      "I77 MM 10 N",                          "+33.991930,-80.954770",
      "I77 MM 10 S",                          "+33.991460,-80.954950",
      "I77 MM 11 N",                          "+34.004450,-80.956790",
      "I77 MM 11 S",                          "+34.004660,-80.957370",
      "I77 MM 12 N",                          "+34.017200,-80.948840",
      "I77 MM 12 S",                          "+34.017340,-80.949290",
      "I77 MM 13 N",                          "+34.030060,-80.940790",
      "I77 MM 13 S",                          "+34.030240,-80.941260",
      "I77 MM 14 N",                          "+34.043370,-80.934400",
      "I77 MM 14 S",                          "+34.043640,-80.935340",
      "I77 MM 15 N",                          "+34.053320,-80.922560",
      "I77 MM 15 S",                          "+34.053300,-80.923190",
      "I77 MM 16 N",                          "+34.067460,-80.921980",
      "I77 MM 16 S",                          "+34.067430,-80.922500",
      "I77 MM 17 N",                          "+34.077960,-80.934540",
      "I77 MM 17 S",                          "+34.077540,-80.934850",
      "I77 MM 18 N",                          "+34.087200,-80.947660",
      "I77 MM 18 S",                          "+34.087020,-80.948000",
      "I77 MM 19 N",                          "+34.099070,-80.957410",
      "I77 MM 19 S",                          "+34.098400,-80.957940",
      "I77 MM 2 N",                           "+33.928820,-81.040150",
      "I77 MM 2 S",                           "+33.929040,-81.036440",
      "I77 MM 20 N",                          "+34.112710,-80.961910",
      "I77 MM 20 S",                          "+34.112750,-80.962530",
      "I77 MM 21 N",                          "+34.127720,-80.962260",
      "I77 MM 21 S",                          "+34.127410,-80.962730",
      "I77 MM 22 N",                          "+34.141220,-80.964430",
      "I77 MM 22 S",                          "+34.141060,-80.965080",
      "I77 MM 23 N",                          "+34.155270,-80.964130",
      "I77 MM 23 S",                          "+34.155020,-80.964760",
      "I77 MM 24 N",                          "+34.169570,-80.969310",
      "I77 MM 24 S",                          "+34.169190,-80.969750",
      "I77 MM 25 N",                          "+34.181510,-80.979420",
      "I77 MM 25 S",                          "+34.181000,-80.979900",
      "I77 MM 26 N",                          "+34.195160,-80.984860",
      "I77 MM 26 S",                          "+34.194980,-80.985300",
      "I77 MM 27 N",                          "+34.209100,-80.983720",
      "I77 MM 27 S",                          "+34.209090,-80.984500",
      "I77 MM 28 N",                          "+34.223080,-80.987440",
      "I77 MM 28 S",                          "+34.222770,-80.987880",
      "I77 MM 29 N",                          "+34.234940,-80.995360",
      "I77 MM 29 S",                          "+34.234680,-80.995760",
      "I77 MM 3 N",                           "+33.929100,-81.024100",
      "I77 MM 3 S",                           "+33.929490,-81.024380",
      "I77 MM 30 N",                          "+34.251250,-80.998810",
      "I77 MM 31 N",                          "+34.265670,-80.997660",
      "I77 MM 32 N",                          "+34.280010,-80.997250",
      "I77 MM 33 N",                          "+34.294670,-80.996700",
      "I77 MM 34 N",                          "+34.308780,-81.000100",
      "I77 MM 35 N",                          "+34.320460,-81.005440",
      "I77 MM 4 N",                           "+33.936200,-81.008910",
      "I77 MM 4 S",                           "+33.936440,-81.009360",
      "I77 MM 41 N",                          "+34.406420,-80.994430",
      "I77 MM 43 N",                          "+34.435240,-80.994290",
      "I77 MM 44 N",                          "+34.449490,-80.991800",
      "I77 MM 44 S",                          "+34.449660,-80.992580",
      "I77 MM 45 N",                          "+34.463880,-80.994620",
      "I77 MM 47 S",                          "+34.492650,-80.996560",
      "I77 MM 50 N",                          "+34.537890,-81.000580",
      "I77 MM 50 S",                          "+34.536820,-81.000920",
      "I77 MM 6 N",                           "+33.950420,-80.981900",
      "I77 MM 6 S",                           "+33.950900,-80.981790",
      "I95 MM 134 S",                         "+33.884030,-80.100320",
      "I95 MM 135 N",                         "+33.897940,-80.095430",
      "I95 MM 136 S",                         "+33.910960,-80.087540",
      "I95 MM 137 N",                         "+33.922270,-80.076610",
      "I95 MM 137 S",                         "+33.922700,-80.077320",
      "I95 MM 138 N",                         "+33.934120,-80.067210",
      "I95 MM 139 N",                         "+33.946480,-80.057610",
      "I95 MM 139 S",                         "+33.946760,-80.057940",
      "I95 MM 140 N",                         "+33.958050,-80.047110",
      "I95 MM 140 S",                         "+33.958300,-80.047540",
      "I95 MM 141 N",                         "+33.971060,-80.041400",
      "I95 MM 142 N",                         "+33.982570,-80.030200",
      "I95 MM 142 S",                         "+33.982910,-80.030560",
      "I95 MM 143 S",                         "+33.991940,-80.016940",
      "I95 MM 144 N",                         "+34.002350,-80.004880",
      "I95 MM 144 S",                         "+34.002620,-80.005250",
      "I95 MM 145 N",                         "+34.014950,-79.996500",
      "I95 MM 145 S",                         "+34.015040,-79.996810",
      "I95 MM 146 N",                         "+34.028960,-79.991250",
      "I95 MM 146 S",                         "+34.029030,-79.991560",
      "I95 MM 160 N",                         "+33.910710,-80.087020"

  });
}
