package net.anei.cadpage.parsers.KY;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class KYCallowayCountyParser extends DispatchA27Parser {

  public KYCallowayCountyParser() {
    super("CALLOWAY COUNTY", "KY");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,cad@callkyso.com";
  }

  private static final Pattern NORTH_PTN = Pattern.compile("\\bNORTH\\b");
  private static final Pattern STREET_PTN = Pattern.compile("\\bSTREET\\b");
  private static final Pattern DRIVE_PTN = Pattern.compile("\\bDRIVE\\b");
  private static final Pattern COURT_PTN = Pattern.compile("\\bCOURT\\b");
  private static final Pattern CIRCLE_PTN = Pattern.compile("\\bCIRCLE\\b");
  private static final Pattern AVENUE_PTN = Pattern.compile("\\bAVENUE\\b");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    address = NORTH_PTN.matcher(address).replaceAll("N");
    address = STREET_PTN.matcher(address).replaceAll("ST");
    address = DRIVE_PTN.matcher(address).replaceAll("DR");
    address = COURT_PTN.matcher(address).replaceAll("CT");
    address = CIRCLE_PTN.matcher(address).replaceAll("CIR");
    address = AVENUE_PTN.matcher(address).replaceAll("AV");
    address = address.replace(" 16 ST", " 16TH ST");
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "90 C C LOWERY DR",                     "+36.628302,-88.294734",
      "1308 CHESTNUT ST",                     "+36.617195,-88.318121",
      "1403 CHESTNUT ST",                     "+36.616135,-88.319804",
      "1415 CHESTNUT ST",                     "+36.615811,-88.321397",
      "1457 CHESTNUT ST",                     "+36.615523,-88.321282",
      "1459 CHESTNUT ST",                     "+36.615713,-88.322963",
      "1503 CHESTNUT ST",                     "+36.615713,-88.322963",
      "1509 CHESTNUT ST",                     "+36.615679,-88.322897",
      "1511 CHESTNUT ST",                     "+36.616417,-88.323998",
      "100 COLLEGE CT",                       "+36.622382,-88.321837",
      "200 COLLEGE CT",                       "+36.621998,-88.322049",
      "300 COLLEGE CT",                       "+36.621548,-88.321641",
      "400 COLLEGE CT",                       "+36.621581,-88.322046",
      "500 COLLEGE CT",                       "+36.621099,-88.321874",
      "600 COLLEGE CT",                       "+36.620817,-88.321705",
      "700 COLLEGE CT",                       "+36.620169,-88.321844",
      "800 COLLEGE CT",                       "+36.620526,-88.322048",
      "900 COLLEGE CT",                       "+36.621121,-88.322387",
      "1000 COLLEGE CT",                      "+36.621575,-88.321242",
      "1100 COLLEGE CT",                      "+36.620961,-88.321242",
      "1200 COLLEGE CT",                      "+36.620535,-88.321255",
      "0 DORM CIR",                           "+36.618263,-88.320921",
      "58 DORM CIR",                          "+36.617188,-88.322025",
      "70 DORM CIR",                          "+36.618292,-88.321608",
      "140 DORM CIR",                         "+36.617402,-88.320712",
      "176 DORM CIR",                         "+36.617535,-88.319584",
      "238 DORM CIR",                         "+36.618664,-88.319455",
      "290 DORM CIR",                         "+36.619501,-88.320618",
      "298 DORM CIR",                         "+36.618664,-88.319455",
      "358 DORM CIR",                         "+36.619541,-88.321911",
      "77 EDUCATION LOOP",                    "+36.614804,-88.322641",
      "99 EDUCATION LOOP",                    "+36.614361,-88.322333",
      "101 EDUCATION LOOP",                   "+36.614105,-88.322486",
      "120 EDUCATION LOOP",                   "+36.612976,-88.322747",
      "125 EDUCATION LOOP",                   "+36.614111,-88.322454",
      "147 EDUCATION LOOP",                   "+36.613406,-88.322511",
      "149 EDUCATION LOOP",                   "+36.613822,-88.322354",
      "150 EDUCATION LOOP",                   "+36.613526,-88.321467",
      "178 EDUCATION LOOP",                   "+36.613017,-88.323613",
      "180 EDUCATION LOOP",                   "+36.613008,-88.322297",
      "400 EDUCATION LOOP",                   "+36.613002,-88.322712",
      "410 EDUCATION LOOP",                   "+36.613416,-88.322481",
      "420 EDUCATION LOOP",                   "+36.614103,-88.322802",
      "500 EDUCATION LOOP",                   "+36.615034,-88.323461",
      "501 EDUCATION LOOP",                   "+36.615042,-88.323474",
      "27 GILBERT GRAVES DR",                 "+36.617224,-88.318206",
      "95 GILBERT GRAVES DR",                 "+36.618082,-88.318173",
      "615 GILBERT GRAVES DR",                "+36.618326,-88.318258",
      "900 GILBERT GRAVES DR",                "+36.621351,-88.318162",
      "901 GILBERT GRAVES DR",                "+36.620967,-88.319279",
      "1004 GILBERT GRAVES DR",               "+36.622894,-88.317755",
      "1005 GILBERT GRAVES DR",               "+36.622659,-88.319997",
      "1600 HAMILTON AV",                     "+36.613397,-88.325761",
      "1601 HAMILTON AV",                     "+36.612649,-88.325723",
      "1401 HIGHWAY 121 BYPASS N",            "+36.622671,-88.320007",
      "1510 MAIN ST",                         "+36.610038,-88.323079",
      "301 N 14TH ST",                        "+36.612954,-88.320366",
      "305 N 14TH ST",                        "+36.613698,-88.320251",
      "307 N 14TH ST",                        "+36.613819,-88.320552",
      "311 N 14TH ST",                        "+36.614543,-88.320609",
      "311 N 14TH ST",                        "+36.614551,-88.320619",
      "201 N 15TH ST",                        "+36.611069,-88.322714",
      "210 N 15TH ST",                        "+36.611576,-88.322281",
      "213 N 15TH ST",                        "+36.611576,-88.322281",
      "215 N 15TH ST",                        "+36.611987,-88.322276",
      "240 N 16TH ST",                        "+36.611044,-88.323723",
      "300 N 16TH ST",                        "+36.611395,-88.323385",
      "302 N 16TH ST",                        "+36.611812,-88.323697",
      "303 N 16TH ST",                        "+36.611569,-88.324706",
      "304 N 16TH ST",                        "+36.611587,-88.323641",
      "307 N 16TH ST",                        "+36.612229,-88.324663",
      "310 N 16TH ST",                        "+36.612396,-88.323601",
      "312 N 16TH ST",                        "+36.612405,-88.323651",
      "313 N 16TH ST",                        "+36.613455,-88.325841",
      "315 N 16TH ST",                        "+36.612537,-88.325829",
      "350 N 16TH ST",                        "+36.612405,-88.323651",
      "380 N 16TH ST",                        "+36.612537,-88.325829",
      "401 N 16TH ST",                        "+36.613816,-88.324753",
      "402 N 16TH ST",                        "+36.613455,-88.325841",
      "500 N 16TH ST",                        "+36.615034,-88.323461",
      "502 N 16TH ST",                        "+36.615206,-88.324036",
      "502 N 16TH ST",                        "+36.615204,-88.324003",
      "503 N 16TH ST",                        "+36.615314,-88.324751",
      "904 N 16TH ST",                        "+36.618349,-88.323667",
      "924 N 16TH ST",                        "+36.622371,-88.324093",
      "926 N 16TH ST",                        "+36.622339,-88.323898",
      "980 N 16TH ST",                        "+36.622371,-88.324093",
      "1402 OLIVE BLVD",                      "+36.612919,-88.320367",
      "1500 OLIVE BLVD",                      "+36.613008,-88.322297",
      "1304 PAYNE ST",                        "+36.615394,-88.317916",
      "1310 PAYNE ST",                        "+36.615397,-88.317923",
      "1317 PAYNE ST",                        "+36.614715,-88.319621",
      "1399 PAYNE ST",                        "+36.614715,-88.319621",
      "1400 PAYNE ST",                        "+36.615694,-88.320303",
      "1507 POPLAR ST EAST",                  "+36.607999,-88.323354",
      "1507 POPLAR ST WEST",                  "+36.608001,-88.323615",
      "1000 RACER DR",                        "+36.620676,-88.320505",
      "1000 RACER DR",                        "+36.620871,-88.320474",
      "50 REGENTS DR",                        "+36.615853,-88.318301",
      "406 REGENTS DR",                       "+36.615842,-88.318314",
      "409 REGENTS DR",                       "+36.616429,-88.319279",
      "2 RESIDENTIAL CIR",                    "+36.617234,-88.322033",
      "3 RESIDENTIAL CIR",                    "+36.618299,-88.321877",
      "4 RESIDENTIAL CIR",                    "+36.617271,-88.320682",
      "5 RESIDENTIAL CIR",                    "+36.618275,-88.320927",
      "6 RESIDENTIAL CIR",                    "+36.617486,-88.319613",
      "8 RESIDENTIAL CIR",                    "+36.618663,-88.319465",
      "10 RESIDENTIAL CIR",                   "+36.619478,-88.320655",
      "12 RESIDENTIAL CIR",                   "+36.619542,-88.321911",
      "525 RUSHING WAY",                      "+36.619376,-88.315585",
      "400 S LP MILLER ST",                   "+36.606873,-88.300139",
      "1500 UNIVERSITY DR",                   "+36.611065,-88.322628",
      "1501 UNIVERSITY DR",                   "+36.610099,-88.322547",
      "1512 UNIVERSITY DR",                   "+36.611046,-88.323718",
      "1520 UNIVERSITY DR",                   "+36.611069,-88.322714",
      "1525 UNIVERSITY DR",                   "+36.610074,-88.322553",
      "1570 UNIVERSITY DR",                   "+36.611044,-88.323723",
      "810 WALDROP DR",                       "+36.618341,-88.322459",
      "810 WALDROP DR",                       "+36.618345,-88.322433",
      "904 WALDROP DR",                       "+36.619654,-88.322476",
      "906 WALDROP DR",                       "+36.619654,-88.322478"
  });
}
