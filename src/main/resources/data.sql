/**
 * Author:  Aamer Abbas
 */

Insert into Accounts(account_id, first_name, last_name, address, balance)
Values 
('ACC334455', 'FName1', 'LName1','Address1', 1000),
('ACC778899', 'FName2', 'LName2','Address2', 1000),
('ACC998877', 'FName3', 'LName3','Address3', 1000); 

Insert into Transactions(transaction_id,from_account,to_account,creation_time,amount,transaction_type,related_transaction)
Values
('TX10001', 'ACC334455', 'ACC778899', parsedatetime('2020-10-20 12:47:55', 'yyyy-MM-dd HH:mm:ss'), 25.00, 'PAYMENT', ''),
('TX10002', 'ACC334455', 'ACC998877', parsedatetime('2020-10-20 17:33:43', 'yyyy-MM-dd HH:mm:ss'), 10.50, 'PAYMENT', ''),
('TX10003', 'ACC998877', 'ACC778899', parsedatetime('2020-10-20 18:00:00', 'yyyy-MM-dd HH:mm:ss'), 5.00, 'PAYMENT', ''),
('TX10004', 'ACC334455', 'ACC998877', parsedatetime('2020-10-20 19:45:00', 'yyyy-MM-dd HH:mm:ss'), 10.50, 'REVERSAL', 'TX10002'),
('TX10005', 'ACC334455', 'ACC778899', parsedatetime('2020-10-20 09:30:00', 'yyyy-MM-dd HH:mm:ss'), 7.25, 'PAYMENT', '');
