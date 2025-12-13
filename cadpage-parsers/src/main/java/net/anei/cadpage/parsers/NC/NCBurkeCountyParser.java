package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Burke County, NC
 */
public class NCBurkeCountyParser extends DispatchOSSIParser {

  public NCBurkeCountyParser() {
    super(CITY_CODES, "BURKE COUNTY", "NC",
          "( CANCEL ADDR CITY? | FYI? SRC ( CITY/Z GPS1! GPS2! | ) CALL ID? CODE? ADDR! X? X? ) INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@bceoc.org,CAD@burke.local";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("|")) body = body.substring(1).trim();
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL|UNDER CONTROL", true);
    if (name.equals("SRC")) return new SourceField("\\S+", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ID")) return new IdField("\\d{5,}", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Z]\\d\\d[A-Za-z]?", true);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");

  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "HICK", "HICKORY",
      "LONG", "LONG VIEW",
      "NEBO", "NEBO",
      "NEWL", "JONAS RIDGE"  // ???
  });
}
