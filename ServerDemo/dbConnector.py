import psycopg2

class dbConnector:
    def connect(self):
        self.conn = psycopg2.connect("dbname=saveMeDB user=postgres")
        self.cur = self.conn.cursor()

        self.cur.execute('SELECT version()')
        print(self.cur.fetchall())
    
    def query(self, sql):
        self.cur.execute(sql)
        self.conn.commit()
        return self.cur.fetchall()

    def do(self, sql):
        self.cur.execute(sql)
        self.conn.commit()

    def close(self):
        self.cur.close()
        self.conn.close()

def initDB():
    global db
    db = dbConnector()
    db.connect()