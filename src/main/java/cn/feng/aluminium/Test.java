package cn.feng.aluminium;

import cn.feng.aluminium.ui.music.api.MusicApi;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class Test {
    public static final String cookie = "MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/weapi/feedback; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/api/feedback; HTTPOnly;NMTID\u003d00OPmZiBT7C17G08EoAgoAXTaYf9jQAAAGR_gsQzA; Max-Age\u003d315360000; Expires\u003dFri, 15 Sep 2034 03:33:43 GMT; Path\u003d/;;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/neapi/feedback; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/neapi/clientlog; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/weapi/clientlog; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/openapi/clientlog; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/api/clientlog; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/eapi/clientlog; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/neapi/feedback; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/wapi/clientlog; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/api/feedback; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/eapi/feedback; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/openapi/clientlog; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/eapi/clientlog; HTTPOnly;__csrf\u003dbde62b635b10987f9032174aae54a515; Max-Age\u003d1296010; Expires\u003dWed, 02 Oct 2024 03:33:53 GMT; Path\u003d/;;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/weapi/feedback; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/weapi/clientlog; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/wapi/feedback; HTTPOnly;MUSIC_SNS\u003d; Max-Age\u003d0; Expires\u003dTue, 17 Sep 2024 03:33:43 GMT; Path\u003d/;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/api/clientlog; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/wapi/feedback; HTTPOnly;MUSIC_R_T\u003d1600492753580; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/neapi/clientlog; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/wapi/clientlog; HTTPOnly;MUSIC_U\u003d00635760BC03D7E5C4CDA9F11FE276849A05EFC4A782D7F694AD0B92BC7D5D767689BF348CFF237AC3FBD7153FFB2485CB767B10C7D1D42F708FFA99551358D3637F201C80570ECD5D5DFFAB58E16BA51E92BE084A00364A650F2F1FEB706BFF4A6218A245EFB4791AFBE979291BF025E97C204D078616693915091BE5132D77E71EF240A9CB7B382427C4F3AA5D736AB3D83053132C353DB63F6847AF80C069804013AE0811D16CBC6174FD1D983F1C28921C98AD721C13AEA8A20BBE2255885C1C01220FC3A7465704B950AE44F25698C9AA380B5CAD55AD50266A39DADCD2106C8788A7F1EDDA4339845E10C4AC643CEA14E4F50BD3C76C5980AC4971199D13A39044C828ED8B73712DF8BF760B79546551A3ECBDA48598BF50021DFD70D5E2ACB072D6FACE3E19E23429FCC47B62EA5FA762F1146E472BEAB8BA29AD03C7082A99EC5C71B28DB9435C9D7820F3A52D; Max-Age\u003d15552000; Expires\u003dSun, 16 Mar 2025 03:33:43 GMT; Path\u003d/; HTTPOnly;MUSIC_A_T\u003d1600492725240; Max-Age\u003d2147483647; Expires\u003dSun, 05 Oct 2092 06:47:50 GMT; Path\u003d/eapi/feedback; HTTPOnly";

    public static void main(String[] args) {
        System.out.println(MusicApi.fetch("/recommend/songs", cookie));
    }
}
