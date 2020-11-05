package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class INHendricksCountyParser extends DispatchA52Parser {

  public INHendricksCountyParser() {
    super((Properties)null, CITY_CODES, "HENDRICKS COUNTY", "IN");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) return false;
    body = body.substring(4).trim();
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
        "AMO",   "AMO",
        "AVN",   "AVON",
        "BBG",   "BROWNSBURG",
        "CAR",   "CARTERSBURG",
        "CLY",   "CLAYTON",
        "CTV",   "COATESVILLE",
        "DAN N", "DANVILLE",
        "DAN S", "DANVILLE",
        "DAN",   "DANVILLE",
        "DNVL",  "DANVILLE",
        "LIZ",   "LIZTON",
        "NSL",   "NORTH SALEM",
        "PLF",   "PLAINFIELD",
        "STI",   "STILESVILLE",

        "46052", "LEBANON",
        "46103", "AMO",
        "46112", "BROWNSBURG",
        "46113", "CAMBY",
        "46118", "CLAYTON",
        "46121", "COATESVILLE",
        "46122", "DANVILLE",
        "46123", "AVON",
        "46149", "LIZTON",
        "46157", "MONROVIA",
        "46158", "MOORESVILLE",
        "46165", "NORTH SALEM",
        "46167", "PITTSBORO",
        "46168", "PLAINFIELD",
        "46180", "STILESVILLE",
        "46183", "WEST NEWTON",
        "46201", "INDIANAPOLIS",
        "46202", "INDIANAPOLIS",
        "46203", "INDIANAPOLIS",
        "46204", "INDIANAPOLIS",
        "46205", "INDIANAPOLIS",
        "46206", "INDIANAPOLIS",
        "46207", "INDIANAPOLIS",
        "46208", "INDIANAPOLIS",
        "46209", "INDIANAPOLIS",
        "46213", "INDIANAPOLIS",
        "46214", "INDIANAPOLIS",
        "46216", "INDIANAPOLIS",
        "46217", "INDIANAPOLIS",
        "46218", "INDIANAPOLIS",
        "46219", "INDIANAPOLIS",
        "46220", "INDIANAPOLIS",
        "46221", "INDIANAPOLIS",
        "46222", "INDIANAPOLIS",
        "46224", "INDIANAPOLIS",
        "46225", "INDIANAPOLIS",
        "46226", "INDIANAPOLIS",
        "46227", "INDIANAPOLIS",
        "46228", "INDIANAPOLIS",
        "46229", "INDIANAPOLIS",
        "46230", "INDIANAPOLIS",
        "46231", "INDIANAPOLIS",
        "46234", "INDIANAPOLIS",
        "46235", "INDIANAPOLIS",
        "46236", "INDIANAPOLIS",
        "46237", "INDIANAPOLIS",
        "46239", "INDIANAPOLIS",
        "46240", "INDIANAPOLIS",
        "46241", "INDIANAPOLIS",
        "46242", "INDIANAPOLIS",
        "46244", "INDIANAPOLIS",
        "46247", "INDIANAPOLIS",
        "46249", "INDIANAPOLIS",
        "46250", "INDIANAPOLIS",
        "46251", "INDIANAPOLIS",
        "46253", "INDIANAPOLIS",
        "46254", "INDIANAPOLIS",
        "46255", "INDIANAPOLIS",
        "46256", "INDIANAPOLIS",
        "46259", "INDIANAPOLIS",
        "46260", "INDIANAPOLIS",
        "46262", "INDIANAPOLIS",
        "46268", "INDIANAPOLIS",
        "46277", "INDIANAPOLIS",
        "46278", "INDIANAPOLIS",
        "46280", "INDIANAPOLIS",
        "46282", "INDIANAPOLIS",
        "46283", "INDIANAPOLIS",
        "46285", "INDIANAPOLIS",
        "46288", "INDIANAPOLIS",
        "46290", "INDIANAPOLIS",
        "46298", "INDIANAPOLIS"
  });
}
