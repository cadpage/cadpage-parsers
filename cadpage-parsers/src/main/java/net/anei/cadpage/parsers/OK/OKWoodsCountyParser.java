package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class OKWoodsCountyParser extends DispatchA65Parser {

  public OKWoodsCountyParser() {
    super(CITY_LIST, "WOODS COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "Dispatch@WoodsOKE911.info,geoconex@nlamerica.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final String[] CITY_LIST = new String[]{
      "ALVA",
      "AVARD",
      "BRINK",
      "CAPRON",
      "DACOMA",
      "FREEDOM",
      "HOPETON",
      "LODER",
      "LOOKOUT",
      "NOEL",
      "WAYNOKA"
  };
}
