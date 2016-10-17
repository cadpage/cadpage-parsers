package net.anei.cadpage.parsers.FL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLHendryCountyParser extends FieldProgramParser {

  public FLHendryCountyParser() {
    super(CITY_LIST, "HENDRY COUNTY", "FL", "HEADER ID CALL ADDR/S ZIP? DISPATCHER SRC? ( WEAPON | INFO ) ADDITIONAL STATUS PRI RECEIVED SHIPPED");
  }
  
  private static Pattern SPLIT = Pattern.compile("( *\n *)+");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(SPLIT.split(body), data);
  }
  
  @Override
  public String getFilter() {
    return "hcdispatch@hendrysheriff.org,smartcop@hendrysheriff.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("HEADER")) return new SkipField("AUTOMATIC CAD NOTIFICATION - *", true);
    if (name.equals("ID")) return new IdField("CAD# *([A-Z0-9]+)", true);
    if (name.equals("CALL")) return new CallField("(?:Call Type|Reference) *(.*?)", true);
    if (name.equals("ADDR")) return new AddressField("Location *(.*?)", true);
    if (name.equals("ZIP")) return new MyCityField("Zip *(\\d{5}|)", true);
    if (name.equals("DISPATCHER")) return new SkipField("Entered by .*", true);
    if (name.equals("SRC")) return new SourceField("District *(.*?)");
    if (name.equals("WEAPON")) return new SkipField("Weapon", true);
    if (name.equals("ADDITIONAL")) return new SkipField("Additional.*", true);
    if (name.equals("STATUS")) return new SkipField("Status.*", true);
    if (name.equals("PRI")) return new PriorityField("Priority *(.*?)", true);
    if (name.equals("RECEIVED")) return new SkipField("Received.*", true);
    if (name.equals("SHIPPED")) return new TimeField("Shipped.*(\\d{2}:\\d{2}:\\d{2})");
    return super.getField(name);
  }
 
  private class MyCityField extends CityField {
    public MyCityField(String pattern, boolean hard) {
      super(pattern, hard);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private static String[] CITY_LIST = new String[]{
      "CLEWISTON",
      "FELDA",
      "FLAGHOLE",
      "HARLEM",
      "IMMOKALEE",
      "LABELLE",
      "LADECA ACRES",
      "MONTURA",
      "PIONEER",
      "PIONEER PLANTATION",
      "PORT LA BELLE"
  };

}
