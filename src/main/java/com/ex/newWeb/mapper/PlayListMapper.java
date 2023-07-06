package com.ex.newWeb.mapper;

import com.ex.newWeb.Dto.PlayListDto;
import com.ex.newWeb.models.PlayList;
import com.ex.newWeb.models.Song;

import java.util.List;
import java.util.stream.Collectors;

import static com.ex.newWeb.mapper.SongMapper.mapToSong;
import static com.ex.newWeb.mapper.SongMapper.mapToSongDto;

public class PlayListMapper {
    public static PlayList mapToPlayList(PlayListDto playListDto){
        PlayList playList = PlayList.builder()
                .id(playListDto.getId())
                .photoUrl(playListDto.getPhotoUrl())
                .name(playListDto.getName())
                .singer(playListDto.getSinger())
                .ratio(playListDto.getRatio())
                .text(playListDto.getText())
                .createdBy(playListDto.getCreatedBy())
                .build();
        return playList;
    }
    public static PlayListDto mapToPlayListDto(PlayList playList){
        PlayListDto playListDto = PlayListDto.builder()
                .id(playList.getId())
                .photoUrl(playList.getPhotoUrl())
                .name(playList.getName())
                .singer(playList.getSinger())
                .ratio(playList.getRatio())
                .text(playList.getText())
                .createdBy(playList.getCreatedBy())
                .songs(playList.getSongSet().stream().map(song -> mapToSongDto(song)).collect(Collectors.toList()))
                .build();
        return playListDto;
    }



}
