import { Component, OnInit } from '@angular/core';
import { ChatListComponent } from "../../components/chat-list/chat-list.component";
import {ChatResponse} from '../../services/models/chat-response';
import { ChatService, MessageService } from '../../services/services';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';
import { MessageResponse } from '../../services/models';
import {DatePipe} from '@angular/common';
import {EmojiData} from '@ctrl/ngx-emoji-mart/ngx-emoji';
import { PickerModule } from '@ctrl/ngx-emoji-mart';


@Component({
  selector: 'app-main',
  imports: [ChatListComponent,
    DatePipe,
    PickerModule
    
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements OnInit {

  selectedChat: ChatResponse = {};
  chats: Array<ChatResponse> = [];
  chatMessages: Array<MessageResponse> = [];
  showEmojis = false;


  constructor(
    private chatService: ChatService,
    private keycloakService: KeycloakService,
    private messageService : MessageService
  ){}

  ngOnInit(): void {
      this.getAllChats();
  }

  private getAllChats() {
    this.chatService.getChatsByReceiver()
    .subscribe({
      next: (res) => {
        this.chats = res;
      }
    })
  }
  logout() {
    this.keycloakService.logout();
  }

  userProfile() {
    this.keycloakService.accountManagement();
  }

  chatSelected(chatResponse: ChatResponse) {
   this.selectedChat  = chatResponse;
   this.getAllChatMessages(chatResponse.id as string);
   this.setMessagesToSeen();
  // this.selectedChat.unreadCount = 0;
  }

  getAllChatMessages(chatId: string){
   this.messageService.getMessages({
    'chat-id': chatId
   }).subscribe({
    next: (messages) =>{
      this.chatMessages = messages;
    }
   })
  }

  setMessagesToSeen(){

  }

  isSelfMessage(message: MessageResponse) {
    return message.senderId === this.keycloakService.userId;
  }

  uploadMedia( target: EventTarget | null) {

  }

  onSelectEmojis( emoji: any){

  }


}
