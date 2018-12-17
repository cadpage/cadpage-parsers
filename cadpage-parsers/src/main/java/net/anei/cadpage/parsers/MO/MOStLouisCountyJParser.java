package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MOStLouisCountyJParser extends DispatchProQAParser {
  
  public MOStLouisCountyJParser() {
    super(MOStLouisCountyParser.CITY_LIST, "ST LOUIS COUNTY", "MO",
          "PRI! CALL! PLACE! EMPTY UNIT EMPTY TIME INFO/SLS+? ID! ADDR! INFO2+", true);
  }
  
  private String infoLine;
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    infoLine = "";
    if (!super.parseMsg(body, data)) return false;
    data.strSupp = append(data.strSupp, "\n", infoLine);
    return true;
  }
  @Override
  public Field getField(String name) {
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|RM|LOT)[- ]+(.*)");
  private class MyInfo2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return;
      }
      
      if (data.strCity.length() == 0 && isCity(field)) {
        data.strCity = field;
        return;
      }
      infoLine = append(infoLine, " / ", data.strSupp);
    }

    @Override
    public String getFieldNames() {
      return "APT CITY INFO";
    }
  }

}
