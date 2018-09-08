using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ClassLibrary.BusinessEntities;

namespace ClassLibrary.BusinessServices
{
    public interface ICheckpointServices
    {
        void Add(Checkpoint checkpoint);
        void Update(Checkpoint checkpoint);
        void Delete(int id);
        Checkpoint Get(int id);
        List<Checkpoint> GetList();
    }
}
