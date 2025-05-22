import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConstants } from "../../constants/app.constants";
import { environment } from '../../../environments/environment';
import { Contact } from '../../model/contact.model';
import { Loans } from '../../model/loans.model';
import { Cards } from '../../model/cards.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http:HttpClient) { }

  getAccountDetails(id: number){
    return this.http.get(environment.rooturl + AppConstants.ACCOUNT_API_URL + "?id="+id.toString(),{ observe: 'response',withCredentials: true });
  }

  getAccountTransactions(customerId: number){
    return this.http.get(environment.rooturl + AppConstants.BALANCE_API_URL+ "?customerId="+customerId.toString(),{ observe: 'response',withCredentials: true });
  }

  getLoansDetails(customerId:number ){
    return this.http.get(environment.rooturl + AppConstants.LOANS_API_URL+ "?customerId="+customerId.toString(),{ observe: 'response',withCredentials: true });
  }

  getCardsDetails(customerId: number){
    return this.http.get(environment.rooturl + AppConstants.CARDS_API_URL+ "?customerId="+customerId.toString(),{ observe: 'response',withCredentials: true });
  }

  getNoticeDetails(){
    return this.http.get(environment.rooturl + AppConstants.NOTICES_API_URL,{ observe: 'response' });
  }

  saveMessage(contact : Contact){
    return this.http.post(environment.rooturl + AppConstants.CONTACT_API_URL,contact,{ observe: 'response',withCredentials: false});
  }

}
