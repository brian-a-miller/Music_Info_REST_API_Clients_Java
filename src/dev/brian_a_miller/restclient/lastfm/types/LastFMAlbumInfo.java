package dev.brian_a_miller.restclient.lastfm.types;

/*
{"artist":"Frank Zappa",
"mbid":"34a5daa7-2090-478b-a5d3-c4e94ede739b",
"tags":{
    "tag":[
        {
          "url":"https:\/\/www.last.fm\/tag\/progressive+rock",
          "name":"progressive rock"
        },
        {
          "url":"https:\/\/www.last.fm\/tag\/instrumental",
          "name":"instrumental"
        },{"url":"https:\/\/www.last.fm\/tag\/rock","name":"rock"},{"url":"https:\/\/www.last.fm\/tag\/jazz","name":"jazz"},{"url":"https:\/\/www.last.fm\/tag\/experimental","name":"experimental"}]},
"playcount":"3810868",
"image":[
          {
            "size":"small",
            "#text":"https:\/\/lastfm.freetls.fastly.net\/i\/u\/34s\/557397c58f19a5a89a003056ab196692.jpg"
          },
          {
            "size":"medium",
            "#text":"https:\/\/lastfm.freetls.fastly.net\/i\/u\/64s\/557397c58f19a5a89a003056ab196692.jpg"},
          {"size":"large","#text":"https:\/\/lastfm.freetls.fastly.net\/i\/u\/174s\/557397c58f19a5a89a003056ab196692.jpg"},
          {"size":"extralarge","#text":"https:\/\/lastfm.freetls.fastly.net\/i\/u\/300x300\/557397c58f19a5a89a003056ab196692.jpg"},
          {"size":"mega","#text":"https:\/\/lastfm.freetls.fastly.net\/i\/u\/300x300\/557397c58f19a5a89a003056ab196692.jpg"},
          {"size":"","#text":"https:\/\/lastfm.freetls.fastly.net\/i\/u\/300x300\/557397c58f19a5a89a003056ab196692.jpg"}],
"tracks":{
            "track":[
              {
                "streamable":{"fulltrack":"0","#text":"0"},
                "duration":220,
                "url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\/Peaches+en+Regalia",
                "name":"Peaches en Regalia",
                "@attr":{"rank":1},
                "artist":{"url":"https:\/\/www.last.fm\/music\/Frank+Zappa","name":"Frank Zappa","mbid":"e20747e7-55a4-452e-8766-7b985585082d"}},
              {"streamable":{"fulltrack":"0","#text":"0"},"duration":126,"url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\/Willie+the+Pimp","name":"Willie the Pimp","@attr":{"rank":2},"artist":{"url":"https:\/\/www.last.fm\/music\/Frank+Zappa","name":"Frank Zappa","mbid":"e20747e7-55a4-452e-8766-7b985585082d"}},
              {"streamable":{"fulltrack":"0","#text":"0"},"duration":538,"url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\/Son+of+Mr.+Green+Genes","name":"Son of Mr. Green Genes","@attr":{"rank":3},"artist":{"url":"https:\/\/www.last.fm\/music\/Frank+Zappa","name":"Frank Zappa","mbid":"e20747e7-55a4-452e-8766-7b985585082d"}},
              {"streamable":{"fulltrack":"0","#text":"0"},"duration":184,"url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\/Little+Umbrellas","name":"Little Umbrellas","@attr":{"rank":4},"artist":{"url":"https:\/\/www.last.fm\/music\/Frank+Zappa","name":"Frank Zappa","mbid":"e20747e7-55a4-452e-8766-7b985585082d"}},
              {"streamable":{"fulltrack":"0","#text":"0"},"duration":1015,"url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\/The+Gumbo+Variations","name":"The Gumbo Variations","@attr":{"rank":5},"artist":{"url":"https:\/\/www.last.fm\/music\/Frank+Zappa","name":"Frank Zappa","mbid":"e20747e7-55a4-452e-8766-7b985585082d"}},
              {"streamable":{"fulltrack":"0","#text":"0"},"duration":315,"url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\/It+Must+Be+a+Camel","name":"It Must Be a Camel","@attr":{"rank":6},"artist":{"url":"https:\/\/www.last.fm\/music\/Frank+Zappa","name":"Frank Zappa","mbid":"e20747e7-55a4-452e-8766-7b985585082d"}}
            ]
          },
"url":"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats",
"name":"Hot Rats",
"listeners":"298753",
"wiki":{
    "published":"03 Mar 2024, 11:44",
    "summary":"Hot Rats is the second solo album by Frank Zappa. It was released in October 1969. The album consists of six songs, five of which are instrumental (the song \"Willie the Pimp\" features a short vocal by Captain Beefheart). It was Zappa's first recording project after the dissolution of the original Mothers of Invention. Because it focuses on long instrumental jazz-like compositions with extensive soloing, the music sounds very different than earlier Zappa albums which featured short songs with satirical vocal performances. It features none of the Mothers, save Ian Underwood, who was also the primary collaborator and sideman. In <a href=\"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\">Read more on Last.fm<\/a>.",
    "content":"Hot Rats is the second solo album by Frank Zappa. It was released in October 1969. The album consists of six songs, five of which are instrumental (the song \"Willie the Pimp\" features a short vocal by Captain Beefheart). It was Zappa's first recording project after the dissolution of the original Mothers of Invention. Because it focuses on long instrumental jazz-like compositions with extensive soloing, the music sounds very different than earlier Zappa albums which featured short songs with satirical vocal performances. It features none of the Mothers, save Ian Underwood, who was also the primary collaborator and sideman. In his original sleeve notes Zappa described the album as \"a movie for your ears.\"\n\nThis was the first Frank Zappa album recorded on 16-track equipment and one of the first of such 16-track recordings released to the public. Recording machines with 16 individual tracks allow for much more flexibility in multi-tracking and overdubbing than the 4 and 8-track tape recorders that were standard in 1969. While Zappa was recording Hot Rats in Los Angeles, The Beatles were working on their Abbey Road album at EMI's soon to be famous Abbey Road Studios in London. By comparison, The Beatles were limited to 8-track technology. Hot Rats still stands out as one of Zappa's greatest musical and technological achievements.\n\nhttp:\/\/en.wikipedia.org\/wiki\/Hot_Rats <a href=\"https:\/\/www.last.fm\/music\/Frank+Zappa\/Hot+Rats\">Read more on Last.fm<\/a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."}}}

 */
public record LastFMAlbumInfo(String artist, String mbid, String playcount, String url, String name, String listeners /* , wiki*/) {
}
