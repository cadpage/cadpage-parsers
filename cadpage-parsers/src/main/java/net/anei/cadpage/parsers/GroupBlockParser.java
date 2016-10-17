package net.anei.cadpage.parsers;

/**
 * Dummy parser that marks the boundary between different parser groups processed by
 * GroupBestParser.  When a GroupBlockParser is encountered, GroupBestParser will
 * the best parser results, if any, found to date.  It will only go on to the next
 * parser group if nothing if the previous parser group succeeded in parsing the page 
 */
public class GroupBlockParser extends MsgParser {

  public GroupBlockParser() {
    super("", "");
  }
}
