package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVBrookeCountyParser extends DispatchA19Parser {

  public WVBrookeCountyParser() {
    super("BROOKE COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "rapid@brooke911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strPlace.isEmpty()) {
      String place = PLACE_TABLE.getProperty(data.strAddress);
      if (place != null) data.strPlace = place;
    }
    return true;
  }

  private static final Properties PLACE_TABLE = buildCodeTable(new String[] {
      "22 BADO PLACE",             "ZETA TAU ALPHA",
      "23 BADO PLACE",             "PHI KAPPA TAU",
      "24 BADO PLACE",             "ALPHA SIGMA PHI",
      "25 BADO PLACE",             "DELTA TAU DELTA",
      "26 BADO PLACE",             "ALPHA XI DELTA",
      "27 BADO PLACE",             "PHI MU",
      "2106 BEALLS RIDGE RD",      "WALDEN'S FARM",
      "2882 BEALLS RIDGE RD",      "BELVEDERE FARM",
      "3122 BEALLS RIDGE RD",      "CLEARVIEW FARM",
      "3647 BEALLS RIDGE RD",      "SCHWERTFEGER CHARLES & NANCY",
      "75 BETA DRIVE",             "WLU-BETA HALL",
      "3114 BETHANY PIKE",         "MAZZELLA WELDING & FABRICATION INC",
      "4135 BETHANY PIKE",         "CRAWFORD'S FARM",
      "4335 BETHANY PIKE",         "RIP-VALE FARM",
      "7229 BETHANY PIKE",         "BETHANY COLLEGE VISITOR CENTER",
      "7329 BETHANY PIKE",         "CAMPBELL MANSION",
      "5 BISON CIRCLE",            "BETHANY COLLEGE RECREATION CNTR",
      "9 BISON CIRCLE",            "HUMMEL FIELDHOUSE",
      "237 CHERRY LN",             "McKINLEYVILLE VOL FIRE DEPT",
      "10 CHRISTMAN DRIVE",        "CAMPBELL VILLAGE 1",
      "11 CHRISTMAN DRIVE",        "CAMPBELL VILLAGE 2",
      "12 CHRISTMAN DRIVE",        "CAMPBELL VILLAGE 4",
      "13 CHRISTMAN DRIVE",        "CAMPBELL VILLAGE 3",
      "15 CHRISTMAN DRIVE",        "CUMMINS COMMUNITY CENTER",
      "17 CHRISTMAN DRIVE",        "GOODNIGHT HOUSE",
      "201 CHURCH ST",             "OLD MEETING HOUSE",
      "208 CHURCH ST",             "BETA THETA PI HOUSE",
      "326 CHURCH ST",             "BETHANY COMMUNITY CENTER",
      "44 COLLEGE ST",             "BETHANY CAFETERIA",
      "45 COLLEGE ST",             "BETHANY HOUSE",
      "30 EAST CAMPUS DRIVE",      "T. W PHILLIPS MEMORIAL LIBRARY",
      "31 EAST CAMPUS DRIVE",      "MORLAN HALL",
      "32 EAST CAMPUS DRIVE",      "HARLAN HALL",
      "33 EAST CAMPUS DRIVE",      "PHILLIPS HALL",
      "34 EAST CAMPUS DRIVE",      "COMMENCEMENT HALL",
      "35 EAST CAMPUS DRIVE",      "OLD MAIN",
      "36 EAST CAMPUS DRIVE",      "KIRKPATRICK HALL",
      "37 EAST CAMPUS DRIVE",      "RICHARDSON HALL",
      "38 EAST CAMPUS DRIVE",      "GRACE PHILLIP JOHNSON ART CNTR",
      "39 EAST CAMPUS DRIVE",      "STEINMAN FINE ARTS CNTR",
      "42 EAST CAMPUS DRIVE",      "CRAMBLET HALL",
      "46 EAST CAMPUS DRIVE",      "COCHRAN HALL",
      "100 FACULTY DRIVE",         "WLU-CAMPBELL HALL",
      "33 FACULTY DRIVE",          "WLU-HUGHES HALL",
      "201 GRESHAM DR",            "HIGHLAND HEARTH",
      "17 HARDER DRIVE",           "HARDER HALL",
      "61 HARDER DRIVE",           "GRESHAM INN",
      "77 HARDER DRIVE",           "MILSOP CONFERENCE CNTR",
      "297 HIGH ST",               "HURL EDUCATION CENTER",
      "2508 LOCUST GROVE RD",      "WINDSOR COAL PORTAL",
      "100 MAIN ST",               "HIBERNIA HOUSE",
      "202 MAIN ST",               "CHAMBERS GENERAL STORE",
      "211 MAIN ST",               "DELTA TAU DELTA FOUNDER HOUSE",
      "303 MAIN ST",               "BETHANY MEMORIAL CHURCH",
      "307 MAIN ST",               "CAMPBELL HALL - B&G",
      "864 MAIN ST",               "HUFF HOUSE",
      "2540 MCADOO RIDGE RD",      "BROOKE CO SPORTSMANS CLUB",
      "40 PETERSON LANE",          "PRESIDENTS HOUSE",
      "200 POINT BREEZE DR",       "OLD ALPHA SIG HOUSE",
      "306 RICHARDSON ST",         "ERICKSON ALUMNI CENTER",
      "1 ROSS ST",                 "BETHANY POST OFFICE",
      "102 SOUTH WAY",             "WLU-KRISE HALL",
      "102 SOUTHWAY",              "WLU-KRISE HALL",
      "133 UNIVERSITY DRIVE",      "WLU-BOYD HALL",
      "279 UNIVERSITY DRIVE",      "WLU-SPORTS & REC",
      "450 UNIVERSITY DRIVE",      "WLU-FINE ARTS",
      "65 UNIVERSITY DRIVE",       "WLU-ROGERS HALL",
      "1333 VAN METER WAY",        "WEST LIBERTY VFD",
      "1495 VAN METER WAY",        "FAMILY DOLLAR-WL",
      "745 VAN METER WAY",         "WEST LIBERTY ELEMETERY",
      "1495 VANMETER WAY",         "FAMILY DOLLAR-WL",
      "745 VANMETER WAY",          "WEST LIBERTY ELEMETERY",
      "1049 W LIBERTY RD",         "FOWLER'S FARM",
      "2199 W LIBERTY RD",         "CHAMBERS MARY WELLS",
      "2619 W LIBERTY RD",         "BETHANY VOL FIRE DEPT",
      "920 W LIBERTY RD",          "COX CAROLE",
      "1001 WASHINGTON PIKE",      "DROVERS INN RESTURANT & TAVERN",
      "1111 WASHINGTON PIKE",      "WELLSBURG TIRE & AUTO",
      "1231 WASHINGTON PIKE",      "FRANKLIN METHODIST CHURCH",
      "1305 WASHINGTON PIKE",      "FRANKLIN INTERMEDIATE SCHOOL",
      "1536 WASHINGTON PIKE",      "PEGGY D'S STORE",
      "1544 WASHINGTON PIKE",      "VERN'S PARADISE LOUNGE",
      "1600 WASHINGTON PIKE",      "HIGHLAND SPRINGS GOLF COURSE",
      "841 WASHINGTON PIKE",       "CHURCH OF THE NAZARENE",
      "960 WASHINGTON PIKE",       "FRANKLIN COMM VOL FIRE DEPT",
      "19 WEST CAMPUS DRIVE",      "WELLNESS CENTER"
  });

}
