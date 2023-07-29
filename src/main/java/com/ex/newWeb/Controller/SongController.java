package com.ex.newWeb.Controller;

import com.ex.newWeb.Dto.PlayListDto;
import com.ex.newWeb.Dto.SongDto;
import com.ex.newWeb.models.PlayList;
import com.ex.newWeb.models.Song;
import com.ex.newWeb.models.UserEntity;
import com.ex.newWeb.security.SecurityUtil;
import com.ex.newWeb.service.SongService;
import com.ex.newWeb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SongController {
    private SongService songService;
    private UserService userService;

    @Autowired
    public SongController(SongService songService, UserService userService) {
        this.songService = songService;
        this.userService = userService;
    }


    @GetMapping("/songs")
    public String songs(Model model){
        List<SongDto> songs = songService.findYourSongs();
        model.addAttribute("songs", songs);
        return "songs-list";
    }

    @GetMapping("/allSongs")
    public String listAllSongs(Model model){

        UserEntity user = new UserEntity();
        List<SongDto> songs = songService.findAllSongs();
        String username = SecurityUtil.getSessionUser();
        if(username != null){
            user = userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("songs", songs);
        return "all-songs-list";
    }
    @GetMapping("/allSongs/search")
    public String searchAllSongs(@RequestParam(value = "query") String query, Model model){
        List<SongDto> songs = songService.searchAllSong(query);
        model.addAttribute("songs", songs);
        return "all-songs-list";
    }
    @GetMapping("/songs/{songId}")
    public String viewSongs(@PathVariable("songId") Long songId, Model model){
        UserEntity user = new UserEntity();
        SongDto songDto = songService.findSongById(songId);
        String username = SecurityUtil.getSessionUser();
        if(username != null){
            user = userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("song",songDto);
        return "song-detail";
    }

    @GetMapping("/songs/search")
    public String searchSong(@RequestParam(value = "query") String query, Model model){
        List<SongDto> songs = songService.searchSong(query);
        model.addAttribute("songs", songs);
        return "songs-list";
    }
    @GetMapping("/songs/{songId}/delete")
    public String songDelete(@PathVariable("songId") Long songId){
        songService.deleteSong(songId);
        return "redirect:/songs";
    }
    @GetMapping("/songs/{songId}/copy")
    public String addCopySong(@PathVariable("songId") Long songId){
        songService.saveCopySong(songId);
        return "redirect:/songs";
    }

    @GetMapping("/songs/new")
    public String newSongForm(Model model){
        Song song = new Song();
        model.addAttribute("song", song);
        return "song-create";
    }


    @PostMapping("/songs/new")
    public String saveSong(@Valid @ModelAttribute("song") SongDto songDto,
                               BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("song", songDto);
            return "song-create";
        }
        songService.saveSong(songDto);
        return "redirect:/songs";
    }
    @GetMapping("/songs/{songId}/edit")
    public String editSong(@PathVariable("songId") Long songId, Model model){
        SongDto songDto = songService.findSongById(songId);
        model.addAttribute("song", songDto);
        return "song-edit";
    }
    @PostMapping("/songs/{songId}/edit")
    public String updateEvent(@PathVariable("songId") Long songId,
                              @Valid @ModelAttribute("song") SongDto songDto,
                              BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("song", songDto);
            return "song-edit";
        }
        songDto.setId(songId);
        songService.updateSong(songDto);
        return "redirect:/songs";
    }



    @GetMapping("/songs/{playListId}/addSong")
    public String addSongPlayList(@PathVariable("playListId") Long playListId, Model model){
        List<SongDto> songs = songService.findYourSongs();
        model.addAttribute("playListId", playListId);
        model.addAttribute("songs", songs);
        return "get-song";
    }
    @GetMapping("/songs/{playListId}/addSong/search")
    public String searchSongInAdd(@RequestParam(value = "query") String query,
                                  @PathVariable("playListId") Long playListId, Model model){
        List<SongDto> songs = songService.searchSong(query);
        model.addAttribute("playListId", playListId);
        model.addAttribute("songs", songs);
        return "get-song";
    }
    @GetMapping("/songs/{playListId}/addSong/{songId}")
    public String addSongToPlayList(@PathVariable("playListId") Long playListId,
                                    @PathVariable("songId") Long songId, Model model){
        songService.addSongPlayList(songId,playListId);
        return "redirect:/playLists/" + playListId;
    }

    @GetMapping("/songs/{playListId}/remove/{songId}")
    public String deleteSongPlayList(@PathVariable("playListId") Long playListId,
                                     @PathVariable("songId") Long songId, Model model){
        songService.deleteSongPlayList(songId,playListId);
        return "redirect:/playLists/" + playListId;
    }




}
