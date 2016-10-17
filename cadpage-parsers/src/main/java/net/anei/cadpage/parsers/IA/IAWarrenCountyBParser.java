package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IAWarrenCountyBParser extends FieldProgramParser {
  
  public IAWarrenCountyBParser() {
    this("WARREN COUNTY", "IA");
  }
  
  IAWarrenCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "ID DATE/d TIME ( TRANSPORT CITY? INFO END | CALL ( ADDR/Z CITY | ) ( PLACE X/Z X/Z MAP! | X X/Z? MAP! | PLACE X/Z MAP! | PLACE MAP! | MAP! | ) INFO/N+ )");
  }
  
  @Override
  public String getAliasCode() {
    return "IAWarrenCountyB";
  }
  
  @Override
  public String getFilter() {
    return "Westcom@wdm-ia.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Westcom:")) return false;
    body = body.substring(8).trim();
    return parseFields(body.split("!"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATE")) return new DateField("\\d\\d-\\d\\d-\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("TRANSPORT")) return new CallField("TRANSPORT - .*", true);
    if (name.equals("MAP")) return new MapField("(?:V[FG]|[CDE]|CLV|UI)\\d+", true);
    return super.getField(name);
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(?:(.*)/)?(\\d{3,})");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSupp = getOptGroup(match.group(1));
      data.strCallId = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO? ID";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldEnd(addr, " INTERSECTN");
    return super.adjustMapAddress(addr);
  }
  
  private static final String[] CITY_LIST = new  String[]{
    
    "WARREN COUNTY",
    
    // Cities
    "ACKWORTH",
    "BEVINGTON",
    "CARLISLE",
    "CUMMING",
    "DES MOINES",
    "HARTFORD",
    "INDIANOLA",
    "LACONA",
    "MARTENSDALE",
    "MILO",
    "NEW VIRGINIA",
    "NORWALK",
    "SANDYVILLE",
    "SPRING HILL",
    "ST. MARYS",
    "WEST DES MOINES",

    // Unincorporated communities
    "BEECH",
    "CHURCHVILLE",
    "COOL",
    "LIBERTY CENTER",
    "PROLE",

    // Townships
    "ALLEN",
    "BELMONT",
    "GREENFIELD",
    "JACKSON",
    "JEFFERSON",
    "LIBERTY",
    "LINCOLN",
    "LINN",
    "OTTER",
    "PALMYRA",
    "RICHLAND",
    "SQUAW",
    "UNION",
    "VIRGINIA",
    "WHITE BREAST",
    "WHITE OAK",
    
    
    "DALLAS COUNTY",
    
    // Cities
    "ADEL",
    "BOUTON",
    "CLIVE",
    "DALLAS CENTER",
    "DAWSON",
    "DE SOTO",
    "DEXTER",
    "GRANGER",
    "GRIMES",
    "LINDEN",
    "MINBURN",
    "PERRY",
    "REDFIELD",
    "URBANDALE",
    "VAN METER",
    "WAUKEE",
    "WEST DES MOINES",
    "WOODWARD",

    // Unincorporated community
    "BOONEVILLE",

    // Townships
    "ADAMS",
    "ADEL",
    "BEAVER",
    "BOONE",
    "COLFAX",
    "DALLAS",
    "DES MOINES",
    "GRANT",
    "LINCOLN",
    "LINN",
    "SPRING VALLEY",
    "SUGAR GROVE",
    "UNION",
    "VAN METER",
    "WALNUT",
    "WASHINGTON"
  };
}
