package net.anei.cadpage.parsers.IA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IABlackHawkCountyParser extends DispatchA47Parser {

  public IABlackHawkCountyParser() {
    super(CITY_LIST, "BLACK HAWK COUNTY", "IA", "\\d{3}[A-Z]?|\\d{1,2}[A-Z]\\d{1,2}|\\dFST");
  }

  @Override
  public String getFilter() {
    return "Swmail@bhcso.org,Xmail@connectingyou.com,dispatch@co.marion.ia.us,messaging@iamresponding.com,bhcpage@bhc911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) data.strSource =  subject;
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("RAYMO")) data.strCity = "RAYMOND";
    else if (data.strCity.equals("COUNTY")) data.strCity = "";
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public String adjustMapAddress(String address) {
    address = FRWY_380_PTN.matcher(address).replaceAll("I 380");
    return super.adjustMapAddress(address);
  }
  private static final Pattern FRWY_380_PTN = Pattern.compile("\\bFR?WY *380\\b|\\b380 *FR?WY");

  private static final String[] CITY_LIST =new String[]{
      // Incorporated cities
      "CEDAR FALLS",
      "DUNKERTON",
      "ELK RUN HEIGHTS",
      "EVANSDALE",
      "GILBERTVILLE",
      "HUDSON",
      "JANESVILLE",
      "JESUP",
      "LA PORTE CITY",
      "RAYMOND",
      "RAYMO",
      "WATERLOO",

      // Unincorporated areas
      "DEWAR",
      "EAGLE CENTER",
      "FINCHFORD",
      "GLASGOW",
      "VOORHIES",
      "WASHBURN",

      // Townships
      "BARCLAY",
      "BENNINGTON",
      "BIG CREEK",
      "BLACK HAWK",
      "CEDAR",
      "CEDAR FALLS",
      "EAGLE",
      "EAST WATERLOO",
      "FOX",
      "LESTER",
      "LINCOLN",
      "MOUNT VERNON",
      "ORANGE",
      "POYNER",
      "SPRING CREEK",
      "UNION",
      "WASHINGTON",

      "COUNTY"

  };
}
