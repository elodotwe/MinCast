package com.jacobarau.mincast.sync.rss;

import com.jacobarau.mincast.subscription.Item;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class RssParserTest {
    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    @Test
    public void testParser() throws ParseException, XmlPullParserException, IOException {
        RssParser parser = new RssParser();

        InputStream istream = this.getClass().getResourceAsStream("/cst.xml");

        ParseResult result = parser.parseRSS(istream, "UTF-8");

        assertEquals(2, result.items.size());
        Item item0 = result.items.get(0);
        assertEquals("CST #463: Mosquito Magnet", item0.getTitle());
        assertEquals("<p><span style=\"font-weight: 400;\">We spend Labor Day weekend at Mom’s. Ben gets a jalopy, Jack Ryan is pretty good and the dangers of comfort in light of the scandals in the Church.</span></p> <p> </p> <p><span style=\"font-weight: 400;\">Movies & TV:</span></p> <p><span style=\"font-weight: 400;\">Jack Ryan (Amazon Prime)</span></p> <p><span style=\"font-weight: 400;\">The Innocents (Netflix)</span></p> <p><span style=\"font-weight: 400;\">Infinity War (BluRay)</span></p> <p> </p> <p><span style=\"font-weight: 400;\">Other:</span></p> <p><a href= \"https://www.amazon.com/Clueless-Galilee-Fresh-Take-Gospels/dp/1681922347/ref=sr_1_1?s=books&ie=UTF8&qid=1535414981&sr=1-1&keywords=clueless+in+Galilee\"> <span style=\"font-weight: 400;\">My Book at Amazon</span></a></p> <p><a href=\"https://www.willitsworks.com/\"><span style= \"font-weight: 400;\">Greg & Jennifer’s Exceptional Together and Exceptional You programs</span></a></p> <p><br /> <br /></p> <p><span style=\"font-weight: 400;\">Please support us through Patreon:</span> <a href=\"http://patreon.com/cst\"><span style= \"font-weight: 400;\">patreon.com/cst</span></a></p> <p><span style=\"font-weight: 400;\">Find us at</span> <a href= \"http://catholicinasmalltown.com\"><span style= \"font-weight: 400;\">catholicinasmalltown.com</span></a></p> <p><span style=\"font-weight: 400;\">Our libsyn page where you can find all our old episodes:</span> <a href= \"http://catholicinasmalltown.libsyn.com/\"><span style= \"font-weight: 400;\">catholicinasmalltown.libsyn.com</span></a></p> <p> </p> <p><span style=\"font-weight: 400;\">Support Ben on his way to Europe</span> <a href= \"http://personal.eftours.com/secure/make-donation.aspx?poid=D3AE7DF3&utm_medium=web&utm_source=paxsecure&utm_campaign=fundraising\"> <span style=\"font-weight: 400;\">here</span></a></p> <p> </p> <p><span style=\"font-weight: 400;\">Theme song by Mary Bragg. Her new CD:</span> <a href=\"http://www.marybragg.com/\"><span style= \"font-weight: 400;\">Lucky Strike</span></a></p> <p> </p> <p><span style=\"font-weight: 400;\">Our other show:</span> <a href= \"http://spoiledwithmacandkatherine.libsyn.com/rss\"><span style= \"font-weight: 400;\">Spoiled! with Mac and Katherine</span></a></p> <p> </p> <p><a href= \"https://www.youtube.com/channel/UCHAP5Jbe4jsatcVvy21om6g?view_as=subscriber\"> <span style=\"font-weight: 400;\">Our son, Ben’s Youtube channel</span></a></p> <p><a href= \"https://www.youtube.com/channel/UCmqu4vCvyzTrALkHNGrDqqw\"><span style=\"font-weight: 400;\"> Our Son Sam’s Youtube channel</span></a></p>",
            item0.getDescription());
        assertEquals(107638809, (long)item0.getEnclosureLengthBytes());
        assertEquals("audio/mpeg", item0.getEnclosureMimeType());
        assertEquals("http://feedproxy.google.com/~r/CatholicInASmallTown/~5/Vf35fWSH1t0/CST_463.mp3",
                item0.getEnclosureUrl());
        assertEquals("2018-09-05T01:22:58Z", item0.getPublishDate().toString());

        Item item1 = result.items.get(1);
        assertEquals("CST #462: Pantless Tubing", item1.getTitle());
        assertEquals("<p><span style=\"font-weight: 400;\">A show two weeks in the making! Sam’s B-day, tubing with Mr. Wilder, working on the basement and a homeschool update. We watch Guernsey potato pie peel club thingie and Katherine reads A People’s History of the Vampire Uprising. Finally, we talk frustrations about last week’s readings… and scandals are bad…and Mac wrote a book.</span></p> <p> </p> <p><span style=\"font-weight: 400;\">Movies & TV:</span></p> <p><span style=\"font-weight: 400;\">The Geurnsey Literary Potato Peel Pie Society</span></p> <p> </p> <p><span style=\"font-weight: 400;\">Books:</span></p> <p><span style=\"font-weight: 400;\">A People’s History of the Vampire Uprising by Raymond A. Villareal</span></p> <p> </p> <p><span style=\"font-weight: 400;\">Other:</span></p> <p><a href= \"https://www.amazon.com/Clueless-Galilee-Fresh-Take-Gospels/dp/1681922347/ref=sr_1_1?s=books&ie=UTF8&qid=1535414981&sr=1-1&keywords=clueless+in+Galilee\"> <span style=\"font-weight: 400;\">My Book at Amazon</span></a></p> <p><a href=\"https://www.willitsworks.com/\"><span style= \"font-weight: 400;\">Greg & Jennifer’s Exceptional Together and Exceptional You programs</span></a></p> <p><br /> <br /></p> <p><span style=\"font-weight: 400;\">Please support us through Patreon:</span> <a href=\"http://patreon.com/cst\"><span style= \"font-weight: 400;\">patreon.com/cst</span></a></p> <p><span style=\"font-weight: 400;\">Find us at</span> <a href= \"http://catholicinasmalltown.com\"><span style= \"font-weight: 400;\">catholicinasmalltown.com</span></a></p> <p><span style=\"font-weight: 400;\">Our libsyn page where you can find all our old episodes:</span> <a href= \"http://catholicinasmalltown.libsyn.com/\"><span style= \"font-weight: 400;\">catholicinasmalltown.libsyn.com</span></a></p> <p> </p> <p><span style=\"font-weight: 400;\">Support Ben on his way to Europe</span> <a href= \"http://personal.eftours.com/secure/make-donation.aspx?poid=D3AE7DF3&utm_medium=web&utm_source=paxsecure&utm_campaign=fundraising\"> <span style=\"font-weight: 400;\">here</span></a></p> <p> </p> <p><span style=\"font-weight: 400;\">Theme song by Mary Bragg. Her new CD:</span> <a href=\"http://www.marybragg.com/\"><span style= \"font-weight: 400;\">Lucky Strike</span></a></p> <p> </p> <p><span style=\"font-weight: 400;\">Our other show:</span> <a href= \"http://spoiledwithmacandkatherine.libsyn.com/rss\"><span style= \"font-weight: 400;\">Spoiled! with Mac and Katherine</span></a></p> <p> </p> <p><a href= \"https://www.youtube.com/channel/UCHAP5Jbe4jsatcVvy21om6g?view_as=subscriber\"> <span style=\"font-weight: 400;\">Our son, Ben’s Youtube channel</span></a></p> <p><a href= \"https://www.youtube.com/channel/UCmqu4vCvyzTrALkHNGrDqqw\"><span style=\"font-weight: 400;\"> Our Son Sam’s Youtube channel</span></a></p>",
            item1.getDescription());
        assertEquals(122816345, (long)item1.getEnclosureLengthBytes());
        assertEquals("audio/mpeg", item1.getEnclosureMimeType());
        assertEquals("http://feedproxy.google.com/~r/CatholicInASmallTown/~5/FQxnJ8NUEqc/CST_462.mp3",
                item1.getEnclosureUrl());
        //28 Aug 2018 02:11:07
        assertEquals("2018-08-28T02:11:07Z", item1.getPublishDate().toString());

        assertEquals("A podcast about a Catholic couple raising kids in a small town.\r\n" +
                "\r\n" +
                "Faith. Family. Fun...Scandalous.", result.subscription.getDescription());
        assertEquals("http://static.libsyn.com/p/assets/5/7/5/d/575d037ad15bd062/CST_pic_4.png", result.subscription.getImageUrl());
        assertEquals("http://catholicinasmalltown.libsyn.com", result.subscription.getLink());
        assertEquals("Catholic in a Small Town", result.subscription.getTitle());
    }
}
