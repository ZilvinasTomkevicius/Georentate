using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class UserCheckpoint
    {
        public int userID { get; set; }
        public int checkpointID { get; set; }
        public bool completed { get; set; }

        public UserCheckpoint(int userid, int checkpointid, bool completed)
        {
            this.userID = userid;
            this.checkpointID = checkpointid;
            this.completed = completed;
        }

        public UserCheckpoint()
        {
        }
    }
}
