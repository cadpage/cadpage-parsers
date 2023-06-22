package net.anei.cadpage.parsers.WA;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAPendOreilleCountyParser extends FieldProgramParser {

  private Set<String> unitSet = new HashSet<String>();

  public WAPendOreilleCountyParser() {
    super(CITY_CODES, "PEND OREILLE COUNTY", "WA",
         "UNIT CALL ADDRCITY UNIT UNIT! INFO+");
  }

  @Override
  public String getFilter() {
    return "dispatch@pendoreille.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equalsIgnoreCase("Email Copy Message From Hiplink")) return false;
    unitSet.clear();
    try {
      if (parseFields(body.split("\n"), 5, data)) return true;
      return data.parseGeneralAlert(this, body);
    } finally {
      unitSet.clear();
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {


    public MyUnitField() {
      super("[A-Z0-9]+", true);
    }
    @Override
    public void parse(String field, Data data) {
      if (!unitSet.add(field)) return;
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static final Pattern STATE_ROUTE_2_PTN = Pattern.compile("\\bSTATE ROUTE 2\\b", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      city = convertCodes(city, CITY_CODES);
      int pt = city.lastIndexOf('/');
      if (pt >= 0) {
        data.strState = city.substring(pt+1);
        city = city.substring(0,pt);
      }
      data.strCity = city;
      String addr = p.get(';');
      data.strPlace = p.get();

      addr = STATE_ROUTE_2_PTN.matcher(addr).replaceAll("US 2");
      parseAddress(addr, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY ST";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("CALLBACK=(.*?) LAT=(.*?) LON=(.*?) UNC=.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1).trim();
        setGPSLoc(match.group(2) + ',' + match.group(3), data);
      }
      else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO PHONE GPS";
    }
  }

  private static Properties CITY_CODES = buildCodeTable(new String[] {
      "AIR",    "Airway Heights/WA",
      "ATH",    "Athol/ID",
      "BLA",    "Blanchard/ID",
      "BOI",    "Boise/ID",
      "BON",    "Bonners Ferry/ID",
      "CRY",    "Careywood/ID",
      "CHA",    "Chattaroy/WA",
      "CHE",    "Cheney/WA",
      "CHW",    "Chewelah/WA",
      "CLK",    "Clark Fork/ID",
      "CDA",    "Coeur d'Alene/ID",
      "CLB",    "Colbert/WA",
      "CFX",    "Colfax/WA",
      "COL",    "Colville/WA",
      "COO",    "Coolin/ID",
      "CSB",    "Coos Bay/OR",
      "CUS",    "Cusick/WA",
      "DG",     "Dalton Gardens/ID",
      "DAV",    "Davenport/WA",
      "DPK",    "Deer Park/WA",
      "EAS",    "Eastport/ID",
      "ELK",    "Elk/WA",
      "FRC",    "Fairchild AFB/WA",
      "FAI",    "Fairfield/WA",
      "FRN",    "Ferndale/WA",
      "FLK",    "Four Lakes/WA",
      "GRN",    "Green Acres/WA",
      "HAY",    "Hayden Lake/ID",
      "HOP",    "Hope/ID",
      "ION",    "Ione/WA",
      "ISS",    "Issaquah/WA",
      "KET",    "Kettle Falls/WA",
      "KOO",    "Kootenai/ID",
      "LAC",    "LaClede/ID",
      "LEW",    "Lewiston/ID",
      "LIB",    "Liberty Lake/WA",
      "LLK",    "Loon Lake/WA",
      "LYN",    "Lynwood/WA",
      "MEA",    "Mead/WA",
      "MED",    "Medical Lake/WA",
      "MET",    "Metaline/WA",
      "MEF",    "Metaline Falls/WA",
      "MLL",    "Mica MIC WA 99023 0Millwood/WA",
      "MSC",    "Moscow/ID",
      "MOS",    "Moses Lake/WA",
      "NAP",    "Naples/ID",
      "NES",    "Nespelem/WA",
      "NLK",    "Newman Lake/WA",
      "NEW",    "Newport/WA",
      "NPT",    "Newport/WA",
      "NMF",    "Nine Mile Falls/WA",
      "NRD",    "Nordman/ID",
      "NOR",    "Northport/WA",
      "OLD",    "Oldtown/ID",
      "OMA",    "Omak/WA",
      "ORO",    "Orofino/ID",
      "OTH",    "Othello/WA",
      "OTO",    "Otis Orchards/WA",
      "PAS",    "Pasco/WA",
      "PEO",    "Peoria/AZ",
      "PST",    "Post Falls/ID",
      "PR",     "Priest River/ID",
      "PUL",    "Pullman/WA",
      "RAT",    "Rathdrum/ID",
      "RAY",    "Raymond/WA",
      "REA",    "REARDAN/WA",
      "REP",    "Republic/WA",
      "RIE",    "Rice/WA",
      "RIC",    "Richland/WA",
      "RIT",    "Ritzville/WA",
      "ROK",    "Rockford/WA",
      "ROC",    "Rockwall/TX",
      "ROS",    "Rosalia/WA",
      "SAG",    "Sagle/ID",
      "SPT",    "Sandpoint/ID",
      "SEA",    "Seattle/WA",
      "SPL",    "Spirit Lake/ID",
      "SPK",    "Spokane/WA",
      "SPV",    "Spokane Valley/WA",
      "SPR",    "Springdale/WA",
      "STV",    "Stevens County/WA",
      "TGR",    "Tiger/WA",
      "TON",    "Tonasket/WA",
      "TUK",    "Tukwila/WA",
      "TUM",    "Tum Tum/WA",
      "USK",    "Usk/WA",
      "VLY",    "Valley/WA",
      "VAL",    "Valleyford/WA",
      "VER",    "Veradale/WA",
      "WAL",    "Walla Walla/WA",
      "WAV",    "Waverly/WA",
      "WIL",    "Wilbur/WA"
  });
}
